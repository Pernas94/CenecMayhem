package com.example.cenecmayhem

import android.content.Intent
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
import com.example.cenecmayhem.cenecMayhem.Batalla
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ronda)


        val userInfo = intent.extras
        if (userInfo != null) {
            if (userInfo.getSerializable("user") != null) {
                user = userInfo.getSerializable("user") as Usuario
            }

            if (userInfo.getSerializable("personaje") != null) {
                personaje = userInfo.getSerializable("personaje") as Personaje

            }
            if (userInfo.getSerializable("enemigos") != null) {
                enemigos = userInfo.getSerializable("enemigos") as ArrayList<Personaje>
            }
        }



        //Intentamos cargar las imagenes. Si algo sale mal, se pone una por defecto
        val arrayImageView:ArrayList<ImageView> = arrayListOf(fotoEnemigo1,fotoEnemigo2,fotoEnemigo3)
        //TODO- COMO GESTIONAR IMAGENES QUE BAJAN DE BBDD? TempFile, guardar en Resources?

        for (view in arrayImageView){
            val storage: StorageReference = Firebase.storage.reference
            val path="cenec/"+personaje?.foto
            storage.child(path)
            val extension: String? = personaje!!.foto.substring(personaje!!.foto.lastIndexOf('.') + 1)
            val localfile = File.createTempFile("tempImage", extension)

            storage.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.path)
                view.setImageBitmap(bitmap)

            }.addOnFailureListener {
                it.printStackTrace()
                view.setImageResource(R.drawable.usuario)
            }
        }


        btnLuchar.setOnClickListener {

            val intent: Intent=Intent(this, Batalla::class.java)
            val bundle:Bundle= Bundle()

            bundle.putSerializable("user", user)
            bundle.putSerializable("personaje", personaje)
            bundle.putSerializable("enemigos", enemigos)

            intent.putExtras(bundle)
            this.startActivity(intent)
        }

    }
}