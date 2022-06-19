package com.example.cenecmayhem

import clases.Usuario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import clases.Ataque
import clases.Personaje
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dao.DAOPersonaje

class Login : AppCompatActivity() {

    val fb: FirebaseFirestore = Firebase.firestore
    val auth: FirebaseAuth = Firebase.auth
    var user: Usuario? = null //Inicializo a null para hacer comprobaciones más adelante

    val editEmail: EditText by lazy { findViewById(R.id.log_editEmail) }
    val editContraseña: EditText by lazy { findViewById(R.id.log_editContraseña) }
    val btnLogin: Button by lazy { findViewById(R.id.log_btnLogin) }
    val btnRegistro:TextView by lazy{findViewById(R.id.log_btnRegistro)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //El usuario logueado se recibe por el Bundle. Compruebo si existe y lo extraigo
        val userInfo = intent.extras

        if (userInfo != null) {
            if (userInfo.getSerializable("user") != null) {
                user = userInfo.getSerializable("user") as Usuario?
                editEmail.text.append(user?.email)

            }
        }

        /**
         * La función onClick de Login:
         *  -Comprueba que los campos no estén vacíos
         *  -Comprueba que el usuario existe en la BBDD. Si no existe, miestra un mensaje de error
         *  -Si el usuario existe, carga la información del usuario y la pasa a la siguiente pantalla, SeleccionJuego
         */
        btnLogin.setOnClickListener {
            if (editEmail.text.isNullOrBlank() || editContraseña.text.isNullOrBlank()) {
                Toast.makeText(this, R.string.camposDebenEstarRellenos, Toast.LENGTH_SHORT).show()
            } else if (!editEmail.text.isNullOrBlank() && !editContraseña.text.isNullOrBlank()) {

                val email = editEmail.text.toString()
                val contraseña = editContraseña.text.toString()

                val task = auth.signInWithEmailAndPassword(email, contraseña)

                task.addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(p0: Task<AuthResult>) {
                        if (task.isSuccessful) {
                            if (user == null) {
                                user = Usuario(email, "")
                            }

                            val b: Bundle = Bundle()
                            b.putString("email", email)
                            val intent = Intent(this@Login, SeleccionJuego::class.java)
                            intent.putExtras(b)
                            this@Login.startActivity(intent)

                        } else {
                            try {
                                throw task.exception!!
                            } catch (e: FirebaseAuthInvalidUserException) {
                                Toast.makeText(
                                    this@Login,
                                    "El usuario no existe",
                                    Toast.LENGTH_LONG
                                ).show()
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(
                                    this@Login,
                                    "Alguno de los datos es incorrecto",
                                    Toast.LENGTH_LONG
                                ).show()
                                editContraseña.text?.clear()
                            } catch (e: Exception) {
                                Toast.makeText(this@Login, e.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                )
            }
        }

        btnRegistro.setOnClickListener {

            val intent=Intent(this, Registro::class.java)
            this.startActivity(intent)
        }

    }


}