package dao

import clases.Usuario
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
        fun crearUsuario(email:String,nombreUsuario: String):Usuario {
            //Subo el usuario a la base de datos
            Log.d("Mau", "Voy a subir al usuario a BBDD")
            fb.collection("usuarios").document(email).set(
                hashMapOf("correo" to email,
                    "nombreusuario" to nombreUsuario
                )
            )

            val user:Usuario=Usuario(email, nombreUsuario)
            return user
        }

        fun inicioSesion(correoElectronico: String,contrasena: String): Task<AuthResult> {
            return auth.signInWithEmailAndPassword(correoElectronico,contrasena)
        }

    }
}