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
import dao.DAOAuth

class Registro : AppCompatActivity() {

    val activity=this
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

                Toast.makeText(activity, R.string.camposDebenEstarRellenos, Toast.LENGTH_SHORT).show()

            }else{

                //Comprobamos si las contraseñas coinciden
                if(editContraseña.text.trim().equals(editConfirmarContraseña.text.trim())){
                    Toast.makeText(activity, R.string.contraseñasNoCoinciden, Toast.LENGTH_SHORT).show()
                    Log.d("Mau", "constraseña 1="+editContraseña.text+" || contraseña2="+editConfirmarContraseña.text+" diferentes?"+(editConfirmarContraseña.text!=editContraseña.text))
                }else{

                    var email:String=editEmail.text.toString()
                    var contraseña:String=editContraseña.text.toString()
                    var usuario:String=editUsuario.text.toString()
                    val task= DAOAuth.registro(email, contraseña, usuario)

                   task.addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                        override fun onComplete(result: Task<AuthResult>) {
                            if (result.isSuccessful) {

                                Log.d("Mau", "Estyo en registro, voy a Crear Usuario desde el DAO")

                                Toast.makeText(activity, (R.string.registroCompletado), Toast.LENGTH_LONG).show()


                                //Voy a la pantalla de login pasando al usuario por bundle

                                val user:Usuario=DAOAuth.crearUsuario(email, usuario)
                                val intent:Intent= Intent(this@Registro, Login::class.java)
                                val bundle:Bundle=Bundle()
                                bundle.putSerializable("user", user)
                                intent.putExtras(bundle)
                                this@Registro.startActivity(intent)


                            }else{
                                Toast.makeText(activity, R.string.registroNoCompletado, Toast.LENGTH_SHORT).show()

                            }
                        }
                    }
                   )
                }
            }
        }
    }
}