package com.example.cenecmayhem



import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import clases.Usuario
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class Registro : AppCompatActivity() {


    val fb:FirebaseFirestore= Firebase.firestore
    val auth: FirebaseAuth = Firebase.auth
    //Lista de personajes jugables desde el principio
    val personajesDisponibles:List<String> = listOf("Alex", "Edu", "Lander", "Norberto", "Robe")

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
                if(!(editContraseña.text.toString().equals(editConfirmarContraseña.text.toString()))){
                    Toast.makeText(this, R.string.contraseñasNoCoinciden, Toast.LENGTH_SHORT).show()
                }else{

                    var email:String=editEmail.text.toString()
                    var contraseña:String=editContraseña.text.toString()
                    var usuario:String=editUsuario.text.toString()
                    val task= auth.createUserWithEmailAndPassword(email, contraseña)

                    task.addOnSuccessListener {
                        fb.collection("usuarios").document(email).set(
                            hashMapOf(
                                "nombreusuario" to usuario,
                                "dinero" to 1000,
                                "pociones" to 5,
                                "coronas" to 0,
                                "vida" to 100,
                                "personajesDisponibles" to personajesDisponibles
                            )
                        ).addOnSuccessListener {
                            var user:Usuario=Usuario(email, usuario)

                            val intent= Intent(this@Registro, Login::class.java)
                            val bundle=Bundle()
                            bundle.putSerializable("user", user)
                            intent.putExtras(bundle)
                            this@Registro.startActivity(intent)
                        }.addOnFailureListener {

                            Toast.makeText(this@Registro, R.string.registroNoCompletado, Toast.LENGTH_SHORT).show()
                        }

                    }.addOnFailureListener {
                            try {
                                throw it
                            } catch (e: FirebaseAuthWeakPasswordException) {
                                Toast.makeText(this, getString(com.example.cenecmayhem.R.string.contrasena_debil), Toast.LENGTH_SHORT).show()
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(this, getString(R.string.email_no_valido), Toast.LENGTH_SHORT).show()
                            } catch (e: FirebaseAuthUserCollisionException) {
                                Toast.makeText(this, getString(R.string.email_repetido), Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(this, getString(R.string.error_desconocido), Toast.LENGTH_SHORT).show()
                            }

                    }
                   task.addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                        override fun onComplete(result: Task<AuthResult>) {
                            if (task.isSuccessful) {

                                Toast.makeText(this@Registro, (R.string.registroCompletado), Toast.LENGTH_LONG).show()

                                //  Creo al usuario en una colección de usuarios de Firebase,
                                // con valores predeterminados para dinero, pociones, coronas y vida

                            }

                        }
                    }
                   )
                }
            }
        }
    }
}