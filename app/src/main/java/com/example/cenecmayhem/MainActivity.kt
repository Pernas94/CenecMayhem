package com.example.cenecmayhem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import clases.Ataque
import clases.Partida
import clases.Personaje
import clases.Usuario
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dao.DAOPartida
import dao.DAOPersonaje

class MainActivity : AppCompatActivity() {

    val irARegistro: Button by lazy{findViewById(R.id.button2)}
    val irALogin:Button by lazy{findViewById(R.id.btnLogin)}
    val irASeleccionarPartida:Button by lazy{findViewById(R.id.btnSeleccionarPartida)}
    val btnAux:Button by lazy{findViewById(R.id.btnAux)}
    //val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        irARegistro.setOnClickListener {
            val intent: Intent =Intent(this, Registro::class.java)
            this.startActivity(intent)
        }

        irALogin.setOnClickListener {
            val intent:Intent=Intent(this, Login::class.java)
            this.startActivity(intent)
        }

        irASeleccionarPartida.setOnClickListener {
            val intent:Intent=Intent(this, SeleccionJuego::class.java)
            val bundle:Bundle=Bundle()
            val user:Usuario= Usuario("pepe@a.es", "", 1000, 5, 0)
            bundle.putSerializable("user", user)
            intent.putExtras(bundle)
            this.startActivity(intent)
        }

        btnAux.setOnClickListener {
            var personajes= generaPersonajesAleatorios(5)
            var partida: Partida =Partida("Naruto Fandom", "Fran", true, "Partida de personajes de Naruto", personajes)
            DAOPartida.guardarPartida(partida)

        }


    }



    fun generaPersonajesAleatorios(numPersonajes:Int):ArrayList<Personaje>{

        var personajes:ArrayList<Personaje> =ArrayList<Personaje>()
        var array= arrayOf("Naruto", "Sasuke", "Kakashi", "Tsunade", "Jiraiya", "Sakura", "Rock Lee", "Madara")
        for (i in 1..numPersonajes){

            var ataques:ArrayList<Ataque> =ArrayList<Ataque>()
            var nombre:String=array[i]
            var atq=nombre.substring(0,nombre.length-1)+"azo"

            for (i in 1..4){
                var ataque:Ataque=Ataque(atq+i, 30, 70,
                    "pega un "+atq, "¡Ha fallado el "+atq+"!");
                ataques.add(ataque)
            }

            var personaje: Personaje =Personaje(nombre, nombre+".jpg", 10000, false, ataques)
            personajes.add(personaje)

        }
        return personajes
    }


}