package com.example.cenecmayhem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Registro : AppCompatActivity() {

    val estaActividad=this
    val auth = FirebaseAuth.getInstance()
    val btn: Button by lazy {  findViewById(R.id.button2)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)


        btn.setOnClickListener {

            //Al usarla dentro de task, la variable t ha de ser declarada final
            val t = auth.createUserWithEmailAndPassword("a@a.es", "123123")
            t.addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                override fun onComplete(p0: Task<AuthResult>) {
                    if (t.isSuccessful) {
                        Toast.makeText(estaActividad, "Completado con éxito", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            })

        }
    }
}