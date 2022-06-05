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


    //Botones del layout
    val btnAtaque1: Button by lazy{findViewById(R.id.bat_btnAtaque1)}
    val btnAtaque2: Button by lazy{findViewById(R.id.bat_btnAtaque2)}
    val btnAtaque3: Button by lazy{findViewById(R.id.bat_btnAtaque3)}
    val btnAtaque4: Button by lazy{findViewById(R.id.bat_btnAtaque4)}
    val btnPocion:ImageButton by lazy{findViewById(R.id.bat_btnPocion)}
    val btnSalir:ImageButton by lazy{findViewById(R.id.bat_btnSalir)}

    //Otros elementos del layout
    val mensaje: TextView by lazy{findViewById(R.id.bat_txtMensaje)}
    val nombrePersonaje:TextView by lazy{findViewById(R.id.bat_nombreJugador)}
    val nombreEnemigo:TextView by lazy{findViewById(R.id.bat_nombreEnemigo)}
    val progPersonaje:ProgressBar by lazy{findViewById(R.id.bat_vidaPersonaje)}
    val progEnemigo:ProgressBar by lazy{findViewById(R.id.bat_vidaEnemigo)}
    val imgPersonaje:ImageView by lazy{findViewById(R.id.bat_imgPersonaje)}
    val imgEnemigo:ImageView by lazy{findViewById(R.id.bat_imgEnemigo)}

    //Conexion BBDD
    val fb: FirebaseFirestore = Firebase.firestore

    //Otros valores
    var vidaJugador:Int=100//Se cargará del bundle
    var vidaEnemigo:Int=100
    val recompensaBatalla:Int=200//Recompensa que se entregará por vencer a un enemigo
    val recompensaRonda:Int=500//Recompensa extra en caso de ganar una ronda completa

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
                nombrePersonaje.text=personaje!!.nombre
                //Hacemos una copia del personaje
                jugador= personaje!!.copy()

            }
            if (userInfo.getSerializable("enemigos") != null) {
                enemigos = userInfo.getSerializable("enemigos") as ArrayList<Personaje>
            }
        }

        //Hacemos una copia del enemigo
        enemigo=enemigos.get(0).copy()
        nombreEnemigo.text=enemigo!!.nombre

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
        progPersonaje.progress=vidaJugador
        progEnemigo.progress=vidaEnemigo
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

        var random:Int=(0..100).random()//Aleatorio para la probabilidad del ataque

        holderMensaje.textAlignment=TEXT_ALIGNMENT_TEXT_START
        if(ataque.probabilidad>=random){

            holderMensaje.text=jugador!!.nombre+ " "+ataque.mensajeAcierto
            vidaEnemigo=vidaEnemigo-ataque.ataque
            progEnemigo.progress=vidaEnemigo

        }else{

            holderMensaje.text=jugador!!.nombre+" "+ ataque.mensajeFallo
        }

        if(vidaEnemigo<=0){
            //Si ganamos, eliminamos al enemigo del array de enemigos y volvemos a pantalla Ronda
            enemigos.removeAt(0)
            if(enemigos.size>0){
                finBatalla(true)
            }else{
                finRonda()
            }

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
        //Se bloquean los botones de ataque del usuario
        cambiaEstadoBoton(btnAtaque1)
        cambiaEstadoBoton(btnAtaque2)
        cambiaEstadoBoton(btnAtaque3)
        cambiaEstadoBoton(btnAtaque4)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                var random =(0..100).random()
                var ataqueRandom:Ataque=enemigo!!.ataques.get((0..4).random())
                holderMensaje.textAlignment=TEXT_ALIGNMENT_TEXT_END

                if(ataqueRandom.probabilidad>=random){
                    holderMensaje.text=enemigo!!.nombre+" " +ataqueRandom.mensajeAcierto
                    vidaJugador=vidaJugador-ataqueRandom.ataque
                    progPersonaje.progress=vidaJugador

                    if(vidaJugador<=0){
                        finBatalla(false)
                    }

                }else{
                    holderMensaje.text=enemigo!!.nombre+" " +ataqueRandom.mensajeFallo

                }
                cambiaEstadoBoton(btnAtaque1)
                cambiaEstadoBoton(btnAtaque2)
                cambiaEstadoBoton(btnAtaque3)
                cambiaEstadoBoton(btnAtaque4)
            },
            3000
        )




    }


    /**
     * Función para finalizar la batalla. Recibe un booleano que determina si gana el usuario o no (true=victoria).
     * En caso de victoria, se vuelve a la pantalla de Ronda para continuar con la ronda.
     * En caso de derrota, se vuelve a la pantalla de Seleccion de Personaje.
     * @param ganaJugador Boolean- true si gana el usuario, false si no
     */
    private fun finBatalla(ganaJugador:Boolean){

        var titulo=""
        var mensaje=""

        //TODO- Actualizar pociones y coronas cuadno esté implementado y subir a BBDD
        //Actualizamos valores del usuario
        user!!.vida=vidaJugador
        if(ganaJugador) user!!.dinero+=recompensaBatalla


        //Se adapta el mensaje a quién haya ganado y se añaden las recompensas
        if(ganaJugador){
            titulo="¡Victoria!"
            mensaje="¡Has vencido!\n ¿Continuar con la siguiente ronda?"

        }else{
            titulo="¡Derrota!"
            mensaje="¡Has perdido!\n ¿Volver a la pantalla de selección de personaje?"
        }

        val mBuilder= AlertDialog.Builder(this@Batalla)
        mBuilder.setTitle(titulo)
        mBuilder.setMessage(mensaje)
        mBuilder.setPositiveButton("Confirmar", DialogInterface.OnClickListener{
                dialog, id->

            var bundle:Bundle=Bundle()
            bundle.putSerializable("user", user)

            if(ganaJugador) {
                //Si gana el jugador, se pasan personaje y enemigos por bundle a la pantalla de Ronda, para continuar.
                bundle.putSerializable("personaje", personaje)
                bundle.putSerializable("enemigos", enemigos)
                val intent:Intent = Intent(this@Batalla, Ronda::class.java)
                intent.putExtras(bundle)
                this.startActivity(intent)

            }else{
                //Si pierde el jugador, solo se pasa información de usuario y se vuelve a la pantalla de selección de personaje
                val intent:Intent = Intent(this@Batalla, SeleccionPersonaje::class.java)
                intent.putExtras(bundle)
                this.startActivity(intent)
            }

        })

        mBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                dialog, id->
            dialog.cancel()
        })

        var alert=mBuilder.create()
        alert.show()

    }

    private fun finRonda(){

        val intent:Intent=Intent(this@Batalla, SeleccionPersonaje::class.java)
        var bundle:Bundle=Bundle()
        bundle.putSerializable("user", user)
        intent.putExtras(bundle)
        this.startActivity(intent)
    }

    /**
     * Función que cambia el estado de un botón de de activo a inactivo
     * @param btn Button- botón que cambiará de estado
     */
    private fun cambiaEstadoBoton(btn:Button){
        btn.isEnabled=!btn.isEnabled
        btn.isClickable=!btn.isClickable
    }


}