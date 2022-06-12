package com.example.cenecmayhem

import android.content.Intent
import clases.Usuario
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.cenecmayhem.cenecMayhem.SeleccionPersonaje
import com.example.cenecmayhem.creadorPartidas.CrearPartida
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SeleccionJuego : AppCompatActivity() {

    var user: Usuario? =
        null //Inicializo a null para comprobar más adelante si el usuario se ha logueado o no
    var email = ""
    val fb: FirebaseFirestore = Firebase.firestore

    val btnCenec: LinearLayout by lazy { findViewById(R.id.sel_btnCenec) }
    val btnPersonalizadas: LinearLayout by lazy { findViewById(R.id.sel_btnPersonalizadas) }
    val btnCrearPartida: TextView by lazy { findViewById(R.id.sel_btnCrearPartida) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_juego)

        //El usuario logueado se recibe por el Bundle. Compruebo si existe y lo extraigo
        val userInfo = intent.extras
        if (userInfo != null) {
            if (userInfo.getString("email") != null) {
                email = userInfo.getString("email") as String
            }
        }



        val docRef = fb.collection("usuarios").document(email)
        docRef.get().addOnSuccessListener { doc ->
            if (doc != null) {

                val usuario: String = doc.data?.get("nombreusuario") as String
                val vida:Long= doc.data?.get("vida") as Long
                val dinero: Long = doc.data?.get("dinero") as Long
                val pociones: Long = doc.data?.get("pociones") as Long
                val coronas: Long = doc.data?.get("coronas") as Long
                val personajesDisponibles:List<String> =doc.data?.get("personajesDisponibles") as List<String>


                user = Usuario(email, usuario, vida.toInt(), dinero.toInt(), pociones.toInt(), coronas.toInt(), personajesDisponibles)


            } else {
                Log.d("Mau", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("Mau", "get failed with ", exception)
            }


        btnCenec.setOnClickListener {
            val intent: Intent = Intent(this, SeleccionPersonaje::class.java)
            val bundle: Bundle = Bundle()
            bundle.putSerializable("user", user)
            intent.putExtras(bundle)
            this.startActivity(intent)
        }

        btnPersonalizadas.setOnClickListener {

        }

        btnCrearPartida.setOnClickListener {
            val intent:Intent=Intent(this@SeleccionJuego, CrearPartida::class.java)
            val bundle:Bundle=Bundle()
            bundle.putSerializable("user", user)
            intent.putExtras(bundle)
            this.startActivity(intent)
        }
    }
}