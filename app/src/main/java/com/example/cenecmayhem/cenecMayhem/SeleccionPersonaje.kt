package com.example.cenecmayhem.cenecMayhem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import clases.Personaje
import clases.Usuario
import com.example.cenecmayhem.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dao.DAOAuth

class SeleccionPersonaje : AppCompatActivity() {

    var user: Usuario? =null
    val fb:FirebaseFirestore= Firebase.firestore

    val txtNombreUsuario:TextView by lazy{findViewById(R.id.selPers_txtUsername)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_personaje)

        val userInfo=intent.extras
        if (userInfo!=null){
            if (userInfo.getSerializable("user") != null) {
                user=userInfo.getSerializable("user") as Usuario?
                Toast.makeText(this, "El usuario recibido es "+user?.usuario+" de "+user?.email, Toast.LENGTH_SHORT).show()
                txtNombreUsuario.text=user?.usuario

            }
        }




        val recyclerView: RecyclerView =findViewById(R.id.selPer_recyclerPersonajes)
        val arrayEventos:ArrayList<Personaje> = ArrayList()
        //fb.collection()


    }
}