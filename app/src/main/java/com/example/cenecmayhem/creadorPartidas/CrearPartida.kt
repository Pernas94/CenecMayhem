package com.example.cenecmayhem.creadorPartidas

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import clases.Partida
import clases.Usuario
import com.example.cenecmayhem.MainActivity
import com.example.cenecmayhem.R
import com.example.cenecmayhem.Ronda
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CrearPartida : AppCompatActivity() {

    //Bundle
    var user:Usuario?=null

    //BBDD
    val fb: FirebaseFirestore = Firebase.firestore

    //Layout y botones
    val txtNombrePartida: TextView by lazy{findViewById(R.id.crPar_nombrePartida)}
    val txtDescripcionPartida:TextView by lazy{findViewById(R.id.crPar_descripcionPartida)}
    val checkPublica:CheckBox by lazy{findViewById(R.id.crPar_publica)}
    val btnContinuar: Button by lazy{findViewById(R.id.crPar_btnContinuar)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_partida)


        val userInfo = intent.extras
        if (userInfo != null) {
            if (userInfo.getSerializable("user") != null) {
                user = userInfo.getSerializable("user") as Usuario

            }
        }


        btnContinuar.setOnClickListener {

            //Compruebo que lo campos no están vacios
            if(txtNombrePartida.text.isNotEmpty()&&txtDescripcionPartida.text.isNotEmpty()){


                //Compruebo si la partida existe en BBDD
                fb.collection("partidas").document(txtNombrePartida.text.toString()).get()
                    .addOnSuccessListener {

                        //Si existe, lo aviso y lo cambio
                        if(it.exists()) {
                            Toast.makeText(this@CrearPartida, "Una partida con ese nombre ya existe, debes escoger otro nombre", Toast.LENGTH_LONG).show()
                            txtNombrePartida.text=""
                        }else{

                            //Si no existe, aviso antes de ir a la próxima pantalla
                            val mBuilder= AlertDialog.Builder(this)
                            mBuilder.setTitle("Aviso")
                            mBuilder.setMessage("Esta información no podrá ser modificada después de pasar a la siguiente pantalla.\n" +
                                    "¿Desea continuar?")
                            mBuilder.setPositiveButton("Confirmar", DialogInterface.OnClickListener{
                                    dialog, id->

                                //Creamos la partida, momentaneamente sin información de personajes
                                var partida: Partida = Partida(txtNombrePartida.text.toString(), user!!.usuario,
                                    checkPublica.isChecked, txtDescripcionPartida.text.toString())

                                val intent: Intent =Intent(this@CrearPartida, CrearJugador::class.java)
                                val bundle:Bundle=Bundle()
                                bundle.putSerializable("user", user)
                                bundle.putSerializable("partida", partida)
                                intent.putExtras(bundle)
                                this.startActivity(intent)
                                this.finish()

                            })

                            mBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                                    dialog, id->
                                dialog.cancel()

                            })

                            var alert=mBuilder.create()
                            alert.show()
                        }
                    }.addOnFailureListener {
                        it.printStackTrace()
                    }

            } else{
                Toast.makeText(this@CrearPartida, "¡Todos los campos deben estar rellenos!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}