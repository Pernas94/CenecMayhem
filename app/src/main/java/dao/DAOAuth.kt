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
                    "nombreusuario" to nombreUsuario,
                    "dinero" to 1000,
                    "pociones" to 5,
                    "coronas" to 0
                )
            )

            Log.d("Mau", "Acabo de crear el usuario en BBDD")

            //Inicializo a cualquier usuario con 1000 monedas,5 pociones y 0 coronas
            val user:Usuario=Usuario(email, nombreUsuario, 1000, 5, 0)

            return user
        }

        fun inicioSesion(correoElectronico: String,contrasena: String): Task<AuthResult> {
            return auth.signInWithEmailAndPassword(correoElectronico,contrasena)
        }

        fun bajarUsuario(email:String){

            fb.collection("usuarios").document(email).get().addOnCompleteListener {
                if(it.isSuccessful){
                    it.addOnSuccessListener {doc->
                        var usuario:String=doc.data?.get("nombreUsuario") as String
                        var vida:Long=doc.data?.get("vida") as Long
                        var dinero:Long=doc.data?.get("dinero") as Long
                        var pociones:Long=doc.data?.get("pociones") as Long
                        var coronas:Long=doc.data?.get("coronas") as Long
                        var personajesdisponibles:List<String> =doc.data?.get("personajesDisponibles") as List<String>

                        val user:Usuario=Usuario(email, usuario,vida.toInt(), dinero.toInt(), pociones.toInt(), coronas.toInt(), personajesdisponibles)
                    }
                }else{
                    //Toast.makeText(this, "Ha habido un error cargando al usuario de BBDD", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun updateUserInfo(user:Usuario?){
            fb.collection("usuarios").document(user!!.email).set(
                
                hashMapOf(
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