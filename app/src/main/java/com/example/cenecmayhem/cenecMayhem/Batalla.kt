package com.example.cenecmayhem.cenecMayhem

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View.TEXT_ALIGNMENT_TEXT_END
import android.view.View.TEXT_ALIGNMENT_TEXT_START
import android.widget.*
import clases.Ataque
import clases.Personaje
import clases.Usuario
import com.example.cenecmayhem.R
import com.example.cenecmayhem.Ronda
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Batalla : AppCompatActivity() {

    //Valores que se recibirán por bundle
    var user: Usuario? = null
    var personaje: Personaje? = null
    var enemigos:ArrayList<Personaje> =ArrayList<Personaje>()
    var jugador:Personaje?=null //Será la copia del jugador
    var enemigo:Personaje?=null //Será la copia del enemigo
    var vidaJugador:Int=100//Se cargará del bundle
    var vidaEnemigo:Int=100


    //Botones del layout
    val btnAtaque1: Button by lazy{findViewById(R.id.bat_btnAtaque1)}
    val btnAtaque2: Button by lazy{findViewById(R.id.bat_btnAtaque2)}
    val btnAtaque3: Button by lazy{findViewById(R.id.bat_btnAtaque3)}
    val btnAtaque4: Button by lazy{findViewById(R.id.bat_btnAtaque4)}
    val btnPocion:ImageButton by lazy{findViewById(R.id.bat_btnPocion)}
    val btnSalir:ImageButton by lazy{findViewById(R.id.bat_btnSalir)}

    //Otros elementos del layout
    val mensaje: TextView by lazy{findViewById(R.id.bat_txtMensaje)}
    val progPersonaje:ProgressBar by lazy{findViewById(R.id.bat_vidaPersonaje)}
    val progEnemigo:ProgressBar by lazy{findViewById(R.id.bat_vidaEnemigo)}
    val imgPersonaje:ImageView by lazy{findViewById(R.id.bat_imgPersonaje)}
    val imgEnemigo:ImageView by lazy{findViewById(R.id.bat_imgEnemigo)}

    //Conexion BBDD
    val fb: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batalla)

        //Cargamos los contenidos que recibimos por bundle
        val userInfo = intent.extras
        if (userInfo != null) {
            if (userInfo.getSerializable("user") != null) {
                user = userInfo.getSerializable("user") as Usuario
                vidaJugador=user!!.vida
            }


            if (userInfo.getSerializable("personaje") != null) {
                personaje = userInfo.getSerializable("personaje") as Personaje
                //Hacemos una copia del personaje
                jugador= personaje!!.copy()

            }
            if (userInfo.getSerializable("enemigos") != null) {
                enemigos = userInfo.getSerializable("enemigos") as ArrayList<Personaje>
            }
        }

        //Hacemos una copia del enemigo
        enemigo=enemigos.get(0).copy()

        //Cargamos los ataques del personaje
        fb.collection("personajes").document(jugador!!.nombre).collection("ataques").get().addOnSuccessListener {
                documents->

            for(document in documents){
                var nombre:String=document.id
                var poderAtaque:Long=document.data.get("ataque") as Long
                var probabilidad:Long=document.data.get("probabilidad") as Long
                var mensajeAcierto:String=document.data.get("mensajeAcierto").toString()
                var mensajeFallo:String=document.data.get("mensajeFallo").toString()

                var ataque: Ataque = Ataque(nombre,poderAtaque.toInt(), probabilidad.toInt(), mensajeAcierto, mensajeFallo)
                jugador!!.ataques.add(ataque)
            }

        }.addOnFailureListener {
            it.printStackTrace()
            it.message
            Toast.makeText(this@Batalla, "Ha habido un error cargando los ataques del personaje", Toast.LENGTH_LONG).show()
        }


        //Cargamos los ataques del enemigo
        fb.collection("personajes").document(enemigo!!.nombre).collection("ataques").get().addOnSuccessListener {
                documents->

            for(document in documents){
                var nombre:String=document.id
                var poderAtaque:Long=document.data.get("ataque") as Long
                var probabilidad:Long=document.data.get("probabilidad") as Long
                var mensajeAcierto:String=document.data.get("mensajeAcierto").toString()
                var mensajeFallo:String=document.data.get("mensajeFallo").toString()

                var ataque: Ataque = Ataque(nombre,poderAtaque.toInt(), probabilidad.toInt(), mensajeAcierto, mensajeFallo)
                enemigo!!.ataques.add(ataque)
            }

            //Rellenamos los botones con los nombres de los ataques
            if(jugador!!.ataques.size>0) {
                btnAtaque1.text = jugador!!.ataques.get(0).nombre
                btnAtaque2.text = jugador!!.ataques.get(1).nombre
                btnAtaque3.text = jugador!!.ataques.get(2).nombre
                btnAtaque4.text = jugador!!.ataques.get(3).nombre
            }

        }.addOnFailureListener {
            it.printStackTrace()
            it.message
            Toast.makeText(this@Batalla, "Ha habido un error cargando los ataques del personaje", Toast.LENGTH_LONG).show()
        }.addOnCompleteListener {

        }


        btnSalir.setOnClickListener {

            val mBuilder= AlertDialog.Builder(this@Batalla)
            mBuilder.setTitle("Salir")
            mBuilder.setMessage("Vas a volver a la pantalla de selección de personaje. Se perderá el progreso de esta ronda ¿Desea continuar?")
            mBuilder.setPositiveButton("Confirmar", DialogInterface.OnClickListener{
                    dialog, id->

                var intent:Intent = Intent(this@Batalla, SeleccionPersonaje::class.java)
                var bundle:Bundle=Bundle()
                bundle.putSerializable("user", user)
                intent.putExtras(bundle)
                this.startActivity(intent)
            })

            mBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                    dialog, id->
                dialog.cancel()
            })
        }

        //Rellenamos los elementos del layout con la información del bundle
        progPersonaje.progress=100
        progEnemigo.progress=100
        imgPersonaje.setImageResource(R.drawable.usuario)
        imgEnemigo.setImageResource(R.drawable.usuario)

        //imgPersonaje.setImageURI(Uri.parse(jugador!!.foto))
        //imgEnemigo.setImageURI(Uri.parse(enemigo!!.foto))


        //Listeners de los botones-> BUCLE DE BATALLA
        btnAtaque1.setOnClickListener {
            onClickAtaque(jugador!!.ataques.get(0), mensaje)
        }

        btnAtaque2.setOnClickListener {
            onClickAtaque(jugador!!.ataques.get(1), mensaje)
        }

        btnAtaque3.setOnClickListener {
            onClickAtaque(jugador!!.ataques.get(2), mensaje)
        }

        btnAtaque4.setOnClickListener {
            onClickAtaque(jugador!!.ataques.get(3), mensaje)
        }

    }

    private fun onClickAtaque(ataque:Ataque, holderMensaje:TextView){

        var random:Int=(0..100).random()

        holderMensaje.textAlignment=TEXT_ALIGNMENT_TEXT_START
        if(ataque.probabilidad>=random){

            holderMensaje.text=jugador!!.nombre+ " "+ataque.mensajeAcierto
            progEnemigo.progress=(progEnemigo.progress-ataque.ataque)
            Log.d("Batalla", "Jugador-> "+ataque.nombre+" exitoso")
        }else{

            holderMensaje.text=ataque.mensajeFallo
            Log.d("Batalla", "Jugador-> "+ataque.nombre+" fallado")
        }

        if(vidaEnemigo<=0){
            //Si ganamos, eliminamos al enemigo del array de enemigos y volvemos a pantalla Ronda
            enemigos.removeAt(0)
            finBatalla("¡Victoria!", "¡Has vencido!\n ¿Continuar con la siguiente ronda?")
        }else{
            //Ataque del enemigo
            ataqueEnemigo(holderMensaje)
        }



    }

    /**
     * Función que realiza el ataque del enemigo de forma automática, iniciando con un delay de dos segundos
     * después del ataque del usuario.
     *
     * @param holderMensaje TextView- Textview donde será mostrado el mensaje de acierto o fallo
     */
    private fun ataqueEnemigo(holderMensaje:TextView) {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                var random =(0..100).random()
                var randomAtk:Int=(0..4).random()
                var ataqueRandom:Ataque=enemigo!!.ataques.get(randomAtk)

                holderMensaje.textAlignment=TEXT_ALIGNMENT_TEXT_END
                if(ataqueRandom.probabilidad>=random){
                    holderMensaje.text=enemigo!!.nombre+" " +ataqueRandom.mensajeAcierto
                    progPersonaje.progress=(progPersonaje.progress-ataqueRandom.ataque)
                    Log.d("Batalla", "Enemigo-> "+ataqueRandom.nombre+" exitoso")

                }else{
                    holderMensaje.text=ataqueRandom.mensajeFallo
                    Log.d("Batalla", "Enemigo-> "+ataqueRandom.nombre+" fallido")
                }
            },
            2000
        )

        if(vidaJugador<=0){
            //Si perdemos, volvemos a la pantalla de seleccion de personaje
            enemigos.removeAt(0)
            finBatalla("¡Victoria!", "¡Has vencido!\n ¿Continuar con la siguiente ronda?")
        }
    }


    private fun finBatalla(titulo:String, mensaje:String){
        val mBuilder= AlertDialog.Builder(this@Batalla)
        mBuilder.setTitle(titulo)
        mBuilder.setMessage(mensaje)
        mBuilder.setPositiveButton("Confirmar", DialogInterface.OnClickListener{
                dialog, id->

            var intent:Intent = Intent(this@Batalla, SeleccionPersonaje::class.java)
            var bundle:Bundle=Bundle()
            bundle.putSerializable("user", user)
            intent.putExtras(bundle)
            this.startActivity(intent)
        })

        mBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                dialog, id->
            dialog.cancel()
        })
    }
}