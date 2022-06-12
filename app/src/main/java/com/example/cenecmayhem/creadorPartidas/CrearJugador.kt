package com.example.cenecmayhem.creadorPartidas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import clases.Partida
import clases.Usuario
import com.example.cenecmayhem.R

class CrearJugador : AppCompatActivity() {

    //Bundle
    var user:Usuario?=null
    var partida: Partida? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_jugador)

        val userInfo = intent.extras
        if (userInfo != null) {
            if (userInfo.getSerializable("user") != null) {
                user = userInfo.getSerializable("user") as Usuario
            }

            if (userInfo.getSerializable("partida")!=null){
                partida=userInfo.getSerializable("partida") as Partida
            }
        }

        Log.d("Mau", "Usuario= "+user!!.usuario)
        Log.d("Mau", partida.toString())



    }
}