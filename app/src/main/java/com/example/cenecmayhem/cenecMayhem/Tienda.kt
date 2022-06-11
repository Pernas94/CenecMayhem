package com.example.cenecmayhem.cenecMayhem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clases.Personaje
import clases.Usuario
import com.example.cenecmayhem.R
import dao.DAOAuth
import utilities.SeleccionPersonajeAdapter
import utilities.TiendaAdapter

class Tienda : AppCompatActivity() {

    //Info por bundle
    var user: Usuario? = null
    var personaje: Personaje? = null
    var enemigos:ArrayList<Personaje> =ArrayList<Personaje>()
    var noDisponibles:ArrayList<Personaje> =ArrayList<Personaje>()

    //Botones y elementos del layout
    val btnRonda:ImageButton by lazy {findViewById(R.id.tien_botonRonda)}
    val btnComprarPocion:ImageButton by lazy{findViewById(R.id.tien_btnComprarPocion)}
    val nombreUsuario:TextView by lazy{findViewById(R.id.tien_nombreUsuario)}
    val infoMonedas:TextView by lazy{findViewById(R.id.tien_infoMonedas)}
    val infoPociones:TextView by lazy{findViewById(R.id.tien_infoPociones)}
    val recycler:RecyclerView by lazy{findViewById(R.id.tien_recyler)}

    //Otros elementos
    val precioPocion=300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tienda)


        val userInfo = intent.extras
        if (userInfo != null) {
            if (userInfo.getSerializable("user") != null) {
                user = userInfo.getSerializable("user") as Usuario
                //Cargamos los valores del usuario en las partes pertinentes
                nombreUsuario.text=user!!.usuario
                refreshUserInfo()
            }

            if (userInfo.getSerializable("personaje") != null) {
                personaje = userInfo.getSerializable("personaje") as Personaje

            }
            if (userInfo.getSerializable("enemigos") != null) {
                enemigos = userInfo.getSerializable("enemigos") as ArrayList<Personaje>
            }
            if(userInfo.getSerializable("noDisponibles")!=null){
                noDisponibles=userInfo.getSerializable("noDisponibles") as ArrayList<Personaje>
            }
        }

        if (noDisponibles.size > 0) {
            val adapter = TiendaAdapter(
                this@Tienda,
                noDisponibles,
                user
            )
            recycler.layoutManager = LinearLayoutManager(this@Tienda)
            recycler.adapter = adapter
        } else {
            Toast.makeText(this@Tienda, "¡No hay más personajes por comprar!", Toast.LENGTH_LONG).show()
        }

        btnComprarPocion.setOnClickListener {
            comprarPocion()
        }
    }



    private fun comprarPocion() {

        if(user!!.dinero>=precioPocion){
            user!!.pociones+=1
            user!!.dinero-=precioPocion
            DAOAuth.updateUserInfo(user)
            Toast.makeText(this@Tienda, "Has comprado una poción!", Toast.LENGTH_LONG).show()
            refreshUserInfo()
        }else{
            Toast.makeText(this@Tienda, "No tienes suficiente dinero!", Toast.LENGTH_LONG).show()
        }
    }




    private fun refreshUserInfo() {
        infoMonedas.text=""+user!!.dinero
        infoPociones.text=""+user!!.pociones
    }
}