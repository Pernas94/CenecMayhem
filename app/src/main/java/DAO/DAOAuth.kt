package DAO

import CLASES.Usuario
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DAOAuth {


    companion object {
        private val auth: FirebaseAuth = Firebase.auth
        private val fb: FirebaseFirestore = Firebase.firestore

        fun registro(correoElectronico:String,contrasena:String,nombreUsuario:String): Task<AuthResult> {
            return auth.createUserWithEmailAndPassword(correoElectronico,contrasena)
        }


        /**
         * Funcion para crear el usuario en la BBDD.
         */
        fun crearUsuario(email:String,nombreUsuario: String): Usuario {
            //Subo el usuario a la base de datos
            Log.d("Mau", "Voy a subir al usuario a BBDD")
            fb.collection("usuarios").document(email).set(
                hashMapOf("correo" to email,
                    "nombreusuario" to nombreUsuario
                )
            ).addOnCompleteListener {  }
            Log.d("Mau", "Usuario subido a BBDD")

            //Creo el objeto usuario para poder usarlo desde AS
            val usuario:Usuario= Usuario(email,nombreUsuario)
            Log.d("Mau", "Usuario creado")
            return usuario
        }

        fun inicioSesion(correoElectronico: String,contrasena: String): Task<AuthResult> {
            return auth.signInWithEmailAndPassword(correoElectronico,contrasena)
        }

    }
}