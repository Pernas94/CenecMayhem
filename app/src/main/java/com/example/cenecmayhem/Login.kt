package com.example.cenecmayhem

import CLASES.Usuario
import DAO.DAOAuth
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    var user:Usuario?=null //Inicializo a null para hacer comprobaciones más adelante

    val editEmail: EditText by lazy{findViewById(R.id.log_editEmail)}
    val editContraseña: EditText by lazy{findViewById(R.id.log_editContraseña)}
    val btnLogin: Button by lazy{findViewById(R.id.log_btnLogin)}
    val btnOlvidadoContraseña: TextView by lazy{findViewById(R.id.log_olvidarContraseña)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        //El usuario logueado se recibe por el Bundle. Compruebo si existe y lo extraigo
        val userInfo=intent.extras

        if (userInfo!=null){
            if (userInfo.getSerializable("user") != null) {
                user=userInfo.getSerializable("user") as Usuario?
                editEmail.text=user?.email as Editable

            }
        }

        btnLogin.setOnClickListener {
            if(editEmail.text.isNullOrBlank()||editContraseña.text.isNullOrBlank()){
                Toast.makeText(this, R.string.camposDebenEstarRellenos, Toast.LENGTH_SHORT).show()
            }else{

                var email=editEmail.text.toString()
                var contraseña=editContraseña.text.toString()

                val task=DAOAuth.inicioSesion(email, contraseña)
                task.addOnCompleteListener(this,
                    OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user: Usuario=Usuario(email,"")
                            val intent:Intent= Intent(this, SeleccionJuego::class.java)
                            val bundle:Bundle=Bundle()
                            bundle.putSerializable("user",user)
                            intent.putExtras(bundle)
                            this.startActivity(intent)


                        } else {
                            Toast.makeText(this,R.string.usuarioOContraselaIncorrecto,Toast.LENGTH_LONG).show()
                        }
                    }
                )
            }
        }

        btnOlvidadoContraseña.setOnClickListener {
            Toast.makeText(this, "POR PROGRAMAR", Toast.LENGTH_SHORT).show()
        }
    }
}