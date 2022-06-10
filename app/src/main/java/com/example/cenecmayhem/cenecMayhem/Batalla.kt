package com.example.cenecmayhem.cenecMayhem

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
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
import dao.DAOAuth


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
    val btnSalir:ImageButton by lazy{findViewById(R.id.bat_btnSalir)}

    //Otros elementos del layout
    val mensajeUsuario: TextView by lazy{findViewById(R.id.bat_txtMensajeUsuario)}
    val mensajeEnemigo: TextView by lazy{findViewById(R.id.bat_txtMensajeEnemigo)}
    val nombrePersonaje:TextView by lazy{findViewById(R.id.bat_nombreJugador)}
    val nombreEnemigo:TextView by lazy{findViewById(R.id.bat_nombreEnemigo)}
    val progPersonaje:ProgressBar by lazy{findViewById(R.id.bat_vidaPersonaje)}
    val progEnemigo:ProgressBar by lazy{findViewById(R.id.bat_vidaEnemigo)}
    val imgPersonaje:ImageView by lazy{findViewById(R.id.bat_imgPersonaje)}
    val imgEnemigo:ImageView by lazy{findViewById(R.id.bat_imgEnemigo)}
    val contDañoPersonaje:TextView by lazy{findViewById(R.id.bat_contDañoPersonaje)}
    val contDañoEnemigo:TextView by lazy{findViewById(R.id.bat_contDañoEnemigo)}

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
            mBuilder.setMessage("Vas a volver a la pantalla de selección de personaje. " +
                    "Se perderá el progreso de esta batalla ¿Desea continuar?")
            mBuilder.setPositiveButton("Confirmar", DialogInterface.OnClickListener{
                    dialog, id->

                //Al salir, guardamos la info de jugador en BBDD y volvemos a seleccion personaje
                DAOAuth.updateUserInfo(user)
                finRonda()

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
            onClickAtaque(jugador!!.ataques.get(0))
        }

        btnAtaque2.setOnClickListener {
            onClickAtaque(jugador!!.ataques.get(1))
        }

        btnAtaque3.setOnClickListener {
            onClickAtaque(jugador!!.ataques.get(2))
        }

        btnAtaque4.setOnClickListener {
            onClickAtaque(jugador!!.ataques.get(3))
        }

    }

    private fun onClickAtaque(ataque:Ataque){

        var random:Int=(0..100).random()//Aleatorio para la probabilidad del ataque
        mensajeEnemigo.text=""
        contDañoPersonaje.text=""
        if(ataque.probabilidad>=random){

            contDañoEnemigo.text="-"+ataque.ataque
            mensajeUsuario.text=jugador!!.nombre+ " "+ataque.mensajeAcierto
            //vidaEnemigo -= ataque.ataque
            vidaEnemigo -= 101
            if (vidaEnemigo<0) vidaEnemigo=0

            ObjectAnimator.ofInt(progEnemigo, "progress", vidaEnemigo).setDuration(1000).start()
            //progEnemigo.progress=vidaEnemigo

        }else{

            mensajeUsuario.text=jugador!!.nombre+" "+ ataque.mensajeFallo
        }

        if(vidaEnemigo<=0){
            //Si ganamos, eliminamos al enemigo del array de enemigos y volvemos a pantalla Ronda.
                //Si no quedan enemigos, se finalizará la ronda
            enemigos.removeAt(0)
            finBatalla(true, enemigos.size <= 0)



        }else{
            //Ataque del enemigo
            ataqueEnemigo()
        }
    }

    /**
     * Función que realiza el ataque del enemigo de forma automática, iniciando con un delay de dos segundos
     * después del ataque del usuario.
     *
     * @param holderMensaje TextView- Textview donde será mostrado el mensaje de acierto o fallo
     */
    private fun ataqueEnemigo() {
        //Se bloquean los botones de ataque del usuario
        cambiaEstadoBoton(btnAtaque1)
        cambiaEstadoBoton(btnAtaque2)
        cambiaEstadoBoton(btnAtaque3)
        cambiaEstadoBoton(btnAtaque4)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                mensajeUsuario.text=""
                contDañoEnemigo.text=""
                var random =(0..100).random()
                var ataqueRandom:Ataque=enemigo!!.ataques.get((0..4).random())
                Log.d("Mau", "Ataque enemigo, Random="+random+" y probabilidad= "+ataqueRandom.probabilidad)
                if(ataqueRandom.probabilidad>=random){
                    mensajeEnemigo.text=enemigo!!.nombre+" " +ataqueRandom.mensajeAcierto
                    contDañoPersonaje.text="-"+ataqueRandom.ataque
                    vidaJugador -= ataqueRandom.ataque
                    if(vidaJugador<0) vidaJugador=0
                    ObjectAnimator.ofInt(progPersonaje, "progress", vidaJugador).setDuration(1500).start()


                    if(vidaJugador<=0){
                        finBatalla(false, true)
                    }

                }else{
                    mensajeEnemigo.text=enemigo!!.nombre+" " +ataqueRandom.mensajeFallo
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
    private fun finBatalla(ganaJugador:Boolean, finRonda:Boolean){

        var titulo=""
        var mensaje=""

        //TODO- Actualizar pociones y coronas cuadno esté implementado y subir a BBDD
        //Actualizamos valores del usuario
        user!!.vida=vidaJugador
        if(ganaJugador) user!!.dinero+=recompensaBatalla
        if(ganaJugador && finRonda) user!!.dinero+=recompensaRonda

        //Se adapta el mensaje a quién haya ganado
        if(ganaJugador && !finRonda){
            titulo="¡Victoria!"
            mensaje="¡Has vencido!\n ¿Continuar con la siguiente ronda?"

        }else if(ganaJugador && finRonda){
            titulo="Fin de ronda"
            mensaje="¡Has vencido a todos tus oponentes!\n ¿Volver a la pantalla de selección de personaje?"
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

            if(ganaJugador && !finRonda) {
                DAOAuth.updateUserInfo(user)
                //Si gana el jugador, se pasan personaje y enemigos por bundle a la pantalla de Ronda, para continuar.
                bundle.putSerializable("personaje", personaje)
                bundle.putSerializable("enemigos", enemigos)
                val intent:Intent = Intent(this@Batalla, Ronda::class.java)
                intent.putExtras(bundle)
                this.startActivity(intent)

            }else if(ganaJugador && finRonda){
                DAOAuth.updateUserInfo(user)
                finRonda()
            }else{

                finRonda()
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

