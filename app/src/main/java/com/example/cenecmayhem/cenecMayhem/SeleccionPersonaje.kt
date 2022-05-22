package com.example.cenecmayhem.cenecMayhem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clases.Personaje
import clases.Usuario
import com.example.cenecmayhem.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dao.DAOAuth
import utilities.SeleccionPersonajeAdapter

class SeleccionPersonaje : AppCompatActivity() {

    var user: Usuario? =null
    val fb:FirebaseFirestore= Firebase.firestore
    val txtNombreUsuario:TextView by lazy{findViewById(R.id.selPers_txtUsername)}



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_personaje)

        val recycler: RecyclerView =findViewById(R.id.selPer_recyclerPersonajes)

        val userInfo=intent.extras
        if (userInfo!=null){
            if (userInfo.getSerializable("user") != null) {
                user=userInfo.getSerializable("user") as Usuario?
                Toast.makeText(this, "El usuario recibido es "+user?.usuario+" de "+user?.email, Toast.LENGTH_SHORT).show()
                txtNombreUsuario.text=user?.usuario
            }
        }


        val disponibles=user?.personajesDisponibles


        if(disponibles!=null){
            val arrayPersonajes:ArrayList<Personaje> =ArrayList<Personaje>()

                val docRef = fb.collection("personajes")
                docRef.get().addOnSuccessListener { documents ->

                    for (doc in documents){
                        Log.d("Mau", "DocumentSnapshot data: ${doc.data}")

                        if(disponibles.contains(doc.id)){
                            val nombre:String=doc.id
                            val foto:String=doc.data?.get("foto") as String
                            val precio:Int=(doc.data?.get("precio") as Long).toInt()
                            //RECIBIR ATAQUES ACÁ?

                            val personaje:Personaje=Personaje(nombre, foto, precio)
                            arrayPersonajes.add(personaje)
                        }
                    }

                    if(arrayPersonajes.size>0){
                        val adapter= SeleccionPersonajeAdapter(this, arrayPersonajes, user)
                        recycler.layoutManager= GridLayoutManager(this@SeleccionPersonaje, 2)
                        recycler.adapter=adapter
                    }else{
                        Log.d("Mau", "No se encontraron personajes para cargar")
                    }


                }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Error cargando personajes de base de datos", Toast.LENGTH_LONG).show()
                    }

        }else{
            Toast.makeText(this@SeleccionPersonaje, "El usuario no tiene personajes para mostrar", Toast.LENGTH_LONG).show()
        }
    }
}