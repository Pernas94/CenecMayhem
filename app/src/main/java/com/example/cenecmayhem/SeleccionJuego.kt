package com.example.cenecmayhem

import CLASES.Usuario
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class SeleccionJuego : AppCompatActivity() {

    var user: Usuario? =null //Inicializo a null para comprobar más adelante si el usuario se ha logueado o no
    val btnCenec: Button by lazy{findViewById(R.id.sel_btnCenec)}
    val btnPersonalizadas: Button by lazy{findViewById(R.id.sel_btnPersonalizadas)}
    val btnCrearPartida: TextView by lazy{findViewById(R.id.sel_btnCrearPartida)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_juego)

        //El usuario logueado se recibe por el Bundle. Compruebo si existe y lo extraigo
        val userInfo=intent.extras
        if (userInfo!=null){
            if (userInfo.getSerializable("user") != null) {
                user=userInfo.getSerializable("user") as Usuario?
            }
        }

        Toast.makeText(this, "Usuario recibido-> "+user?.usuario+"  "+user?.email, Toast.LENGTH_SHORT).show()


        btnCenec.setOnClickListener {

        }

        btnPersonalizadas.setOnClickListener {

        }

        btnCrearPartida.setOnClickListener{


        }
    }
}