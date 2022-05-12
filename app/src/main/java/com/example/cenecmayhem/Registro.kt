package com.example.cenecmayhem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Registro : AppCompatActivity() {

    val activity=this
    val auth = FirebaseAuth.getInstance()
    val btnRegistro: Button by lazy {  findViewById(R.id.btnRegistro)}
    val editUsuario: EditText by lazy {findViewById(R.id.editUsuario)}
    val editEmail: EditText by lazy {findViewById(R.id.editEmail)}
    val editContraseña: EditText by lazy {findViewById(R.id.editContraseña)}
    val editConfirmarContraseña: EditText by lazy {findViewById(R.id.editConfirmarContraseña)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)



        btnRegistro.setOnClickListener {

            //Comprobamos si los campos están rellenos.
            if(editUsuario.text.isBlank()||editEmail.text.isBlank()||editContraseña.text.isBlank()||editConfirmarContraseña.text.isBlank()){

                Toast.makeText(activity, R.string.camposDebenEstarRellenos, Toast.LENGTH_SHORT).show()

            }else{

                //Comprobamos si las contraseñas coinciden
                if(!editContraseña.text.equals(editConfirmarContraseña.text)){
                    Toast.makeText(activity, R.string.contraseñasNoCoinciden, Toast.LENGTH_SHORT).show()
                }else{

                    //Creamos el usuario
                    val t = auth.createUserWithEmailAndPassword(editEmail.text.toString(), editContraseña.text.toString())
                    t.addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                        override fun onComplete(p0: Task<AuthResult>) {
                            if (t.isSuccessful) {
                                Toast.makeText(activity, R.string.registroCompletado, Toast.LENGTH_LONG).show()
                            }
                            if(t.isCanceled){
                                Toast.makeText(activity, R.string.registroNoCompletado, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }






        }
    }
}