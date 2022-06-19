package dao

import clases.Usuario
import android.util.Log
import android.widget.Toast
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

        /**
         * Función para crear un usuario con método de autenticación.
         * @param correoElectronico String- Email dell usuario
         * @param contrasena String- Contraseña del ususario
         * @param nombreUsuario String- nombre del ususario
         * @return Task<AuthResult>- Resultado de la actividad
         */
        fun registro(correoElectronico:String,contrasena:String,nombreUsuario:String): Task<AuthResult> {
            return auth.createUserWithEmailAndPassword(correoElectronico,contrasena)
        }


        /**
         * Funcion para crear el usuario en la BBDD. Inicializa valores fijos, al ser
         * la creación del usuario.
         */
        fun crearUsuario(email:String,nombreUsuario: String):Usuario {
            //Subo el usuario a la base de datos
            Log.d("Mau", "Voy a subir al usuario a BBDD")
            fb.collection("usuarios").document(email).set(
                hashMapOf("correo" to email,
                    "nombreusuario" to nombreUsuario,
                    "dinero" to 1000,
                    "pociones" to 5,
                    "coronas" to 0
                )
            )

            //Inicializo a cualquier usuario con 1000 monedas,5 pociones y 0 coronas
            val user:Usuario=Usuario(email, nombreUsuario, 1000, 5, 0)

            return user
        }

        /**
         * Función de prueba para iniciar sesión.
         * @param correoElectronico String- correo con el que iniciar sesión
         * @param contrasena String- constraseña del ususario
         * @return Task<AuthResult>- Resultado de la actividad
         */
        fun inicioSesion(correoElectronico: String,contrasena: String): Task<AuthResult> {
            return auth.signInWithEmailAndPassword(correoElectronico,contrasena)
        }


        /**
         * Función que actualiza los parámetros del ususario en BBDD.
         * @param user Usuario?- Usuario a actualizar.
         */
        fun updateUserInfo(user:Usuario?){

            fb.collection("usuarios").document(user!!.email).set(
                hashMapOf(
                    "nombreusuario" to user!!.usuario,
                    "vida" to user!!.vida,
                    "dinero" to user!!.dinero,
                    "coronas" to user!!.coronas,
                    "personajesDisponibles" to user!!.personajesDisponibles,
                    "pociones" to user!!.pociones
                )
            )
        }

    }
}