package com.example.cenecmayhem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    val auth= FirebaseAuth.getInstance()

    val editEmail: EditText by lazy{findViewById(R.id.log_editEmail)}
    val editContraseña: EditText by lazy{findViewById(R.id.log_editContraseña)}
    val btnLogin: Button by lazy{findViewById(R.id.log_btnLogin)}
    val btnOlvidadoContrasela: TextView by lazy{findViewById(R.id.log_olvidarContraseña)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        btnLogin.setOnClickListener {
            if(editEmail.text.isNullOrBlank()||editContraseña.text.isNullOrBlank()){
                Toast.makeText(this, R.string.camposDebenEstarRellenos, Toast.LENGTH_SHORT).show()
            }else{

                var email=editEmail.text.toString()
                var contraseña=editContraseña.text.toString()

                auth.signInWithEmailAndPassword(email,contraseña)
                    .addOnCompleteListener(this,
                        OnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.getCurrentUser()
                                Toast.makeText(this,user?.email+" : "+user?.uid,Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this,R.string.usuarioOContraselaIncorrecto,Toast.LENGTH_LONG).show()
                            }
                        })

            }
        }

        btnOlvidadoContrasela.setOnClickListener {
            Toast.makeText(this, "POR PROGRAMAR", Toast.LENGTH_SHORT).show()
        }
    }
}