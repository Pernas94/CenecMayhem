package com.example.cenecmayhem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import clases.Personaje
import clases.Usuario

class Ronda : AppCompatActivity() {

    var user:Usuario?=null
    var personaje: Personaje?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ronda)



        val userInfo=intent.extras
        if (userInfo!=null){
            if (userInfo.getSerializable("user") != null) {
                user=userInfo.getSerializable("user") as Usuario
                Toast.makeText(this, "El usuario recibido es "+user?.usuario+" de "+user?.email, Toast.LENGTH_SHORT).show()
            }

            if(userInfo.getSerializable("personaje")!=null){
                personaje=userInfo.getSerializable("personaje") as Personaje
                Toast.makeText(this, "El PERSONAJE recibido es "+personaje?.nombre, Toast.LENGTH_SHORT).show()
            }
        }





    }
}