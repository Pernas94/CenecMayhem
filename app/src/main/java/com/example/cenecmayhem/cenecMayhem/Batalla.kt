package com.example.cenecmayhem.cenecMayhem

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import clases.Personaje
import clases.Usuario
import com.example.cenecmayhem.R

class Batalla : AppCompatActivity() {

    //Valores que se recibirán por bundle
    var user: Usuario? = null
    var personaje: Personaje? = null
    var enemigos:ArrayList<Personaje> =ArrayList<Personaje>()

    //Botones del layout
    val btnAtaque1: Button by lazy{findViewById(R.id.bat_btnAtaque1)}
    val btnAtaque2: Button by lazy{findViewById(R.id.bat_btnAtaque2)}
    val btnAtaque3: Button by lazy{findViewById(R.id.bat_btnAtaque3)}
    val btnAtaque4: Button by lazy{findViewById(R.id.bat_btnAtaque4)}
    val mensaje: TextView by lazy{findViewById(R.id.bat_txtMensaje)}
    val btnPocion:Button by lazy{findViewById(R.id.bat_btnPocion)}
    val btnSalir:Button by lazy{findViewById(R.id.bat_btnSalir)}

    //Otros elementos del layout
    val vidaPersonaje:ProgressBar by lazy{findViewById(R.id.bat_vidaPersonaje)}
    val vidaEnemigo:ProgressBar by lazy{findViewById(R.id.bat_vidaEnemigo)}
    val imgPersonaje:ImageView by lazy{findViewById(R.id.bat_imgPersonaje)}
    val imgEnemigo:ImageView by lazy{findViewById(R.id.bat_imgEnemigo)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batalla)

        //Cargamos los contenidos que recibimos por bundle
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
            if (userInfo.getSerializable("enemigos") != null) {
                enemigos = userInfo.getSerializable("enemigos") as ArrayList<Personaje>
                for (enem in enemigos){
                    Log.d("Mau", "Enemigo-> "+enem.nombre)
                }
            }
        }


        //Rellenamos los elementos del layout con la información del bundle

        btnAtaque1.text=personaje!!.ataques.get(0).nombre
        btnAtaque2.text=personaje!!.ataques.get(1).nombre
        btnAtaque3.text=personaje!!.ataques.get(2).nombre
        btnAtaque4.text=personaje!!.ataques.get(3).nombre

        vidaPersonaje.progress=user!!.vida
        vidaEnemigo.progress=50
        imgPersonaje.setImageURI(Uri.parse(personaje!!.foto))
        imgEnemigo.setImageURI(Uri.parse(enemigos.get(0).foto))


    }
}