package com.example.cenecmayhem



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import clases.Usuario

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dao.DAOAuth
import java.util.*

class Registro : AppCompatActivity() {


    val fb:FirebaseFirestore= Firebase.firestore
    //Lista de personajes jugables desde el principio
    val personajesDisponibles:List<String> = listOf("Pepe1", "Pepe2", "Pepe3", "Pepe4", "Pepe5", "Pepe6")

    val btnRegistro: Button by lazy {  findViewById(R.id.btnRegistro)}
    val editUsuario: EditText by lazy {findViewById(R.id.editUsuario)}
    val editEmail: EditText by lazy {findViewById(R.id.editEmail)}
    val editContraseña: EditText by lazy {findViewById(R.id.editContraseña)}
    val editConfirmarContraseña: EditText by lazy {findViewById(R.id.editConfirmarContraseña)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)


        /**
         * Función que realiza el registro del usuario en la aplicación y en la base de datos:
         *  -Comprueba si los campos están rellenos
         *  -Comprueba si las contraseñas aportadas coinciden
         *  -Comprueba si el registro se ha realizado con éxito
         *      -Si se ha realizaod con éxito, sube la información del usuario a la base de datos
         */
        btnRegistro.setOnClickListener {

            //Comprobamos si los campos están rellenos.
            if(editUsuario.text.isBlank()||editEmail.text.isBlank()||editContraseña.text.isBlank()||editConfirmarContraseña.text.isBlank()){

                Toast.makeText(this, R.string.camposDebenEstarRellenos, Toast.LENGTH_SHORT).show()

            }else{

                //Comprobamos si las contraseñas coinciden
                if(editContraseña.text.trim()==(editConfirmarContraseña.text.trim())){
                    Toast.makeText(this, R.string.contraseñasNoCoinciden, Toast.LENGTH_SHORT).show()
                    Log.d("Mau", "constraseña 1="+editContraseña.text+" || contraseña2="+editConfirmarContraseña.text+" diferentes?"+(editConfirmarContraseña.text!=editContraseña.text))
                }else{

                    var email:String=editEmail.text.toString()
                    var contraseña:String=editContraseña.text.toString()
                    var usuario:String=editUsuario.text.toString()
                    val task= DAOAuth.registro(email, contraseña, usuario)

                   task.addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                        override fun onComplete(result: Task<AuthResult>) {
                            if (task.isSuccessful) {

                                Toast.makeText(this@Registro, (R.string.registroCompletado), Toast.LENGTH_LONG).show()

                                //Creo al usuario en una colección de usuarios de Firebase, con valores predeterminados para dinero, pociones y coronas
                                fb.collection("usuarios").document(email).set(
                                    hashMapOf("correo" to email,
                                        "nombreusuario" to usuario,
                                        "dinero" to 1000,
                                        "pociones" to 5,
                                        "coronas" to 0,
                                        "personajesDisponibles" to personajesDisponibles
                                    )
                                ).addOnCompleteListener {
                                    if(it.isSuccessful){

                                        var user:Usuario=Usuario(email, usuario)

                                        val intent:Intent= Intent(this@Registro, Login::class.java)
                                        val bundle:Bundle=Bundle()
                                        bundle.putSerializable("user", user)
                                        intent.putExtras(bundle)
                                        this@Registro.startActivity(intent)

                                    }else{
                                        Toast.makeText(this@Registro, "ERROOOOOOOOR", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }else{
                                Toast.makeText(this@Registro, R.string.registroNoCompletado, Toast.LENGTH_SHORT).show()

                            }
                        }
                    }
                   )
                }
            }
        }
    }
}