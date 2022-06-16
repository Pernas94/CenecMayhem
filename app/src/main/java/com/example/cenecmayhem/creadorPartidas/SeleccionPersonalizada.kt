package com.example.cenecmayhem.creadorPartidas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clases.Partida
import clases.Usuario
import com.example.cenecmayhem.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import utilities.SeleccionPersonajeAdapter
import utilities.SeleccionPersonalizadaAdapter

class SeleccionPersonalizada : AppCompatActivity() {

    var user: Usuario? = null
    val fb: FirebaseFirestore = Firebase.firestore
    val txtNombreUsuario: TextView by lazy { findViewById(R.id.partPer_usuario) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_personalizada)

        //Cargo valores iniciales de recycler y de lista de partidas
        val recycler: RecyclerView = findViewById(R.id.partPer_recyclerPartidas)
        val partidas:ArrayList<Partida> = ArrayList<Partida>()



        val userInfo = intent.extras
        if (userInfo != null) {
            if (userInfo.getSerializable("user") != null) {
                user = userInfo.getSerializable("user") as Usuario?

                txtNombreUsuario.text = user?.usuario
            }
        }

        val docRef = fb.collection("partidas")
        docRef.get().addOnSuccessListener { documents ->

            for(doc in documents){

                var nombrePartida=doc.data.get("nombrePartida") as String
                var creador=doc.data.get("creador") as String
                var publica=doc.data.get("publica") as Boolean
                var descripcion=doc.data.get("descripcion") as String

                //nombre, creador, publica, desc, personajes
                var partida: Partida =Partida( nombrePartida, creador, publica, descripcion)
                partidas.add(partida)
                Log.d("Mau", "Bajada partida "+partida.toString())
            }

            if (partidas.size > 0) {
                val adapter = SeleccionPersonalizadaAdapter(this, user, partidas)

                recycler.layoutManager = LinearLayoutManager(this)
                recycler.adapter = adapter
            } else {
                Log.d("Mau", "Error cargando las partidas")
            }

        }





    }
}