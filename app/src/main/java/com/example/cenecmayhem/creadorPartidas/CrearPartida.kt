package com.example.cenecmayhem.creadorPartidas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import clases.Partida
import clases.Usuario
import com.example.cenecmayhem.MainActivity
import com.example.cenecmayhem.R

class CrearPartida : AppCompatActivity() {

    //Bundle
    var user:Usuario?=null



    //Layout y botones
    val txtNombrePartida: TextView by lazy{findViewById(R.id.crPar_nombrePartida)}
    val txtDescripcionPartida:TextView by lazy{findViewById(R.id.crPar_descripcionPartida)}
    val checkPublica:CheckBox by lazy{findViewById(R.id.crPar_publica)}
    val btnContinuar: Button by lazy{findViewById(R.id.crPar_btnContinuar)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_partida)


        val userInfo = intent.extras
        if (userInfo != null) {
            if (userInfo.getSerializable("user") != null) {
                user = userInfo.getSerializable("user") as Usuario

            }
        }


        btnContinuar.setOnClickListener {

            if(txtNombrePartida.text.isNotEmpty()&&txtDescripcionPartida.text.isNotEmpty()){

                //Creamos la partida, momentaneamente sin información de personajes
                var partida: Partida = Partida(txtNombrePartida.text.toString(), user!!.usuario,checkPublica.isChecked, txtDescripcionPartida.text.toString())

                val intent: Intent =Intent(this@CrearPartida, CrearJugador::class.java)
                val bundle:Bundle=Bundle()
                bundle.putSerializable("user", user!!)
                bundle.putSerializable("partida", partida)
                intent.putExtras(bundle)
                this.startActivity(intent)
            }else{
                Toast.makeText(this@CrearPartida, "¡Todos los campos deben estar rellenos!", Toast.LENGTH_SHORT).show()
            }
        }



    }


}