package com.example.cenecmayhem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import clases.Ataque
import clases.Personaje
import clases.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dao.DAOPersonaje
import utilities.SeleccionPersonajeAdapter

class Ronda : AppCompatActivity() {

    var user: Usuario? = null
    var personaje: Personaje? = null
    val fb: FirebaseFirestore = Firebase.firestore
    val txtNombreUsuario: TextView by lazy { findViewById(R.id.selPers_txtUsername) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ronda)


        val userInfo = intent.extras
        if (userInfo != null) {
            if (userInfo.getSerializable("user") != null) {
                user = userInfo.getSerializable("user") as Usuario
                Toast.makeText(
                    this,
                    "El usuario recibido es " + user?.usuario + " de " + user?.email,
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (userInfo.getSerializable("personaje") != null) {
                personaje = userInfo.getSerializable("personaje") as Personaje
                Toast.makeText(
                    this,
                    "El PERSONAJE recibido es " + personaje?.nombre,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        //Sacar dos personajes aleatorios que NO sean boss
        val enemigos: ArrayList<Personaje> = ArrayList<Personaje>()


        val docRef = fb.collection("personajes").whereEqualTo("boss", false)
        docRef.get().addOnSuccessListener { documents ->

            documents.inde
            for (doc in documents) {
                Log.d("Mau", "DocumentSnapshot data: ${doc.data}")

                val nombre: String = doc.id
                val foto: String = doc.data?.get("foto") as String
                val precio: Int = (doc.data?.get("precio") as Long).toInt()
                val boss:Boolean=doc.data?.get("boss") as Boolean
                //RECIBIR ATAQUES ACÁ?

                var ataques:ArrayList<Ataque> =ArrayList<Ataque>()

                //RECORRO LOS ATAQUES DE CADA PERSONAJE
                fb.collection("personajes").document(nombre).collection("ataques").get().addOnSuccessListener {
                        documents->

                    for(document in documents){

                        var nombre:String=document.id
                        var poderAtaque:Long=document.data.get("ataque") as Long
                        var probabilidad:Long=document.data.get("probabilidad") as Long
                        var mensajeAcierto:String=document.data.get("mensajeAcierto").toString()
                        var mensajeFallo:String=document.data.get("mensajeFallo").toString()


                        var ataque: Ataque = Ataque(nombre,poderAtaque.toInt(), probabilidad.toInt(), mensajeAcierto, mensajeFallo)
                        ataques.add(ataque)
                    }

                }
                val personaje: Personaje = Personaje(nombre, foto, precio, ataques,boss)
                enemigos.add(personaje)

            }
        }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error cargando personajes de base de datos",
                    Toast.LENGTH_LONG
                ).show()
            }


    }
}