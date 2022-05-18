package com.example.cenecmayhem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import clases.Ataque
import clases.Personaje
import clases.Usuario
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

            var array:ArrayList<Personaje> =DAOPersonaje.bajarTodosPersonajes()

            if(array.size>0){
                Toast.makeText(this, array.get(0).toString(), Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Vacio", Toast.LENGTH_SHORT).show()
            }
        }


    }
    fun generaPersonajesAleatorios(numPersonajes:Int){


        for (i in 1..numPersonajes){

            var ataques:ArrayList<Ataque> =ArrayList<Ataque>()

            for (i in 1..4){
                var ataque:Ataque=Ataque("Pepazo"+i, 30, 70, "te ha dado un pepazo", "ha fallado el pepazo");
                ataques.add(ataque)
            }

            var personaje: Personaje =Personaje("Pepe"+i, "", true)
            DAOPersonaje.guardarPersonaje(personaje)
        }
    }
}