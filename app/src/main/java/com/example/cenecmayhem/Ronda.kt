package com.example.cenecmayhem

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import clases.Ataque
import clases.Personaje
import clases.Usuario
import com.example.cenecmayhem.cenecMayhem.Batalla
import com.example.cenecmayhem.cenecMayhem.Tienda
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dao.DAOAuth
import dao.DAOPersonaje
import utilities.SeleccionPersonajeAdapter
import java.io.File

class Ronda : AppCompatActivity() {



    //Info por bundle
    var user: Usuario? = null
    var personaje: Personaje? = null
    var enemigos:ArrayList<Personaje> =ArrayList<Personaje>()
    var noDisponibles:ArrayList<Personaje> =ArrayList<Personaje>()


    //Imagenes y botones
    val fotoEnemigo1:ImageView by lazy{findViewById(R.id.ron_imgEnemigo1)}
    val fotoEnemigo2:ImageView by lazy{findViewById(R.id.ron_imgEnemigo2)}
    val fotoEnemigo3:ImageView by lazy{findViewById(R.id.ron_imgEnemigo3)}
    val btnLuchar: Button by lazy{findViewById(R.id.ron_btnLuchar)}
    val btnTienda:ImageButton by lazy{findViewById(R.id.ron_btnTienda)}
    val btnBeberPocion:ImageButton by lazy{findViewById(R.id.ron_btnBeberPocion)}

    //Otros elementos
    val infoVida:TextView by lazy{findViewById(R.id.ron_infoVida)}
    val infoMonedas:TextView by lazy{findViewById(R.id.ron_infoMonedas)}
    val infoPociones:TextView by lazy{findViewById(R.id.ron_infoPociones)}
    val nombreUsuario:TextView by lazy{findViewById(R.id.ron_nombreUsuario)}

    //EXTRAS/////////////////////////////////////////////
    var curaPocion:Int=30


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
            if(userInfo.getSerializable("noDisponibles")!=null){
                noDisponibles=userInfo.getSerializable("noDisponibles") as ArrayList<Personaje>
            }
        }

        //Cargo la información del usuario en los bloques de información
        nombreUsuario.text=user!!.usuario
        refreshUserInfo()


        //TODO- COMO GESTIONAR IMAGENES QUE BAJAN DE BBDD? TempFile, guardar en Resources?
        var cont=0
        val storage: StorageReference = Firebase.storage.reference
        //Intentamos cargar las imagenes. Si algo sale mal, se pone una por defecto
        Log.d("Mau", "Enemigos size="+enemigos.size)
        val arrayImageView:ArrayList<ImageView> = arrayListOf(fotoEnemigo3)
        if(enemigos.size>1) arrayImageView.add(fotoEnemigo2)
        if(enemigos.size>2) arrayImageView.add(fotoEnemigo1)

        for (view in arrayImageView){
            val path="cenec/"+enemigos.get(cont).foto
            storage.child(path)
            val extension: String? = enemigos.get(cont).foto.substring(enemigos.get(cont).foto.lastIndexOf('.') + 1)
            val localfile = File.createTempFile(enemigos.get(cont).nombre, extension)
            cont++
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
            this.finish()
        }

        btnTienda.setOnClickListener{
            val intent:Intent=Intent(this@Ronda, Tienda::class.java)
            val bundle:Bundle=Bundle()
            bundle.putSerializable("user", user)
            bundle.putSerializable("noDisponibles", noDisponibles)
            bundle.putSerializable("personaje", personaje)
            bundle.putSerializable("enemigos", enemigos)
            intent.putExtras(bundle)
            this.startActivity(intent)

        }

        btnBeberPocion.setOnClickListener {
            beberPocion()
        }

    }

    private fun refreshUserInfo() {
        infoVida.text=""+user!!.vida
        infoMonedas.text=""+user!!.dinero
        infoPociones.text=""+user!!.pociones
    }


    /**
     * Función que permite al usuario beber una poción para restaurar la salud si esta es menor a 0.
     * Consume una poción, aumenta la vida y actualiza los valores tanto en el layout como en la basde de datos.
     */
    private fun beberPocion(){

        if(user!!.pociones>0&&user!!.vida<100){
            var vidaGanada:Int=100-user!!.vida
            if(vidaGanada>curaPocion) vidaGanada=curaPocion

            user!!.pociones-=1
            user!!.vida+=curaPocion
            if(user!!.vida>100) user!!.vida=100
            DAOAuth.updateUserInfo(user)
            Toast.makeText(this@Ronda, "Has ganado "+vidaGanada+" puntos de vida!", Toast.LENGTH_SHORT).show()
            refreshUserInfo()

        }else if(user!!.pociones>0&&user!!.vida>=100){
            Toast.makeText(this@Ronda, "Tienes la vida a tope!", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this@Ronda, "No te quedan pociones!", Toast.LENGTH_LONG).show()
        }
    }
}