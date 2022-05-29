package com.example.cenecmayhem

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import clases.Ataque
import clases.Personaje
import clases.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dao.DAOPersonaje
import utilities.SeleccionPersonajeAdapter
import java.io.File

class Ronda : AppCompatActivity() {

    var user: Usuario? = null
    var personaje: Personaje? = null
    var enemigos:ArrayList<Personaje> =ArrayList<Personaje>()

    val fotoEnemigo1:ImageView by lazy{findViewById(R.id.ron_imgEnemigo1)}
    val fotoEnemigo2:ImageView by lazy{findViewById(R.id.ron_imgEnemigo2)}
    val fotoEnemigo3:ImageView by lazy{findViewById(R.id.ron_imgEnemigo3)}
    val btnLuchar: Button by lazy{findViewById(R.id.ron_btnLuchar)}

    val fb: FirebaseFirestore = Firebase.firestore
    //val txtNombreUsuario: TextView by lazy { findViewById(R.id.selPers_txtUsername) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ronda)


        val userInfo = intent.extras
        if (userInfo != null) {
            if (userInfo.getSerializable("user") != null) {
                user = userInfo.getSerializable("user") as Usuario
                Toast.makeText(
                    this,
                    "El usuario recibido es " + user?.usuario + " de " + user?.email,
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (userInfo.getSerializable("personaje") != null) {
                personaje = userInfo.getSerializable("personaje") as Personaje
                Toast.makeText(
                    this,
                    "El PERSONAJE recibido es " + personaje?.nombre,
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (userInfo.getSerializable("enemigos") != null) {
                enemigos = userInfo.getSerializable("enemigos") as ArrayList<Personaje>
                for (enem in enemigos){
                    Log.d("Mau", "Enemigo-> "+enem.nombre)
                }
            }
        }

        val arrayImageView:ArrayList<ImageView> = arrayListOf(fotoEnemigo1,fotoEnemigo2,fotoEnemigo3)

        for (view in arrayImageView){
            val storage: StorageReference = Firebase.storage.reference
            val path="cenec/"+personaje?.foto
            storage.child(path)
            val extension: String? = personaje!!.foto.substring(personaje!!.foto.lastIndexOf('.') + 1)
            val localfile = File.createTempFile("tempImage", extension)

            //TODO- Solucionar problema de carga de imagenes
            storage.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.path)
                view.setImageBitmap(bitmap)

            }.addOnFailureListener {
                it.printStackTrace()
                view.setImageResource(R.drawable.usuario)
            }
        }

        btnLuchar.setOnClickListener {
            Toast.makeText(this@Ronda, "Vas a luchar como "+personaje?.nombre+" contra "+enemigos.get(0).nombre, Toast.LENGTH_SHORT).show()
        }

    }
}