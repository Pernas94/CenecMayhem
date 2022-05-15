package com.example.cenecmayhem

import android.content.Intent
import clases.Usuario
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.cenecmayhem.cenecMayhem.SeleccionPersonaje

class SeleccionJuego : AppCompatActivity() {

    var user: Usuario? =null //Inicializo a null para comprobar más adelante si el usuario se ha logueado o no
    val btnCenec: LinearLayout by lazy{findViewById(R.id.sel_btnCenec)}
    val btnPersonalizadas: LinearLayout by lazy{findViewById(R.id.sel_btnPersonalizadas)}
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
            val intent: Intent =Intent(this, SeleccionPersonaje::class.java)
            val bundle:Bundle=Bundle()
            bundle.putSerializable("user", user)
            intent.putExtras(bundle)
            this.startActivity(intent)

        }

        btnPersonalizadas.setOnClickListener {

        }

        btnCrearPartida.setOnClickListener{


        }
    }
}