package com.example.cenecmayhem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val estaActividad=this
    val auth = FirebaseAuth.getInstance()
    val btn: Button by lazy {  findViewById(R.id.button2)}
    //val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {

            //Al usarla dentro de task, la variable t ha de ser declarada final
            val t = auth.createUserWithEmailAndPassword("Pernas94@gmail.es", "123123")
            t.addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                override fun onComplete(p0: Task<AuthResult>) {
                    if (t.isSuccessful) {
                        Toast.makeText(estaActividad, "Completado con éxito", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            })



                        /*
                        // Create a new user with a first and last name
                        val user = hashMapOf(
                            "first" to "Ada",
                            "last" to "Lovelace",
                            "born" to 1815
                        )

            // Add a new document with a generated ID
                        db.collection("users")
                            .add(user)
                            .addOnSuccessListener { documentReference ->
                                Log.d("FB", "DocumentSnapshot added with ID: ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.w("FB", "Error adding document", e)
                            }*/
        }

    }

}