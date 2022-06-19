package com.example.cenecmayhem

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import clases.Ataque
import clases.Partida
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
    var partida: Partida?=null

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
            if(userInfo.getSerializable("partida")!=null){
                partida=userInfo.getSerializable("partida") as Partida
            }
        }

        //Cargo la información del usuario en los bloques de información
        nombreUsuario.text=user!!.usuario
        refreshUserInfo()

        //Controlamos qué imágenes tienen que cargarse
        if(enemigos.size>=3){
            putImage(enemigos.get(2), fotoEnemigo1)
        }
        if(enemigos.size>=2){
            putImage(enemigos.get(1), fotoEnemigo2)
        }
        if(enemigos.size>=1){
            putImage(enemigos.get(0), fotoEnemigo3)
        }
/*

        var cont=0
        val storage: StorageReference = Firebase.storage.reference
        //Intentamos cargar las imagenes. Si algo sale mal, se pone una por defecto
        val arrayImageView:ArrayList<ImageView> = arrayListOf(fotoEnemigo3)
        if(enemigos.size>1) arrayImageView.add(fotoEnemigo2)
        if(enemigos.size>2) arrayImageView.add(fotoEnemigo1)
        for (view in arrayImageView){
            /*
            val path="cenec/"+enemigos.get(cont).foto
            storage.child(path)
            val extension: String? = enemigos.get(cont).foto.substring(enemigos.get(cont).foto.lastIndexOf('.') + 1)
            val localfile = File.createTempFile(enemigos.get(cont).nombre, extension)
            cont++
            storage.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.path)
                view.setImageBitmap(bitmap)

            }.addOnFailureListener {

                //view.setColorFilter(ContextCompat.getColor(this@Ronda, R.color.lightRedCM));
            }*/
            val imagename:String = enemigos.get(cont).foto.substring(0, enemigos.get(cont).foto.lastIndexOf("."))
            Log.d("Mau", "Nombre de imagen->"+imagename)
            val res: Int = resources.getIdentifier(imagename, "drawable", packageName)
            if(res!=0){

                view.setImageDrawable(resources.getDrawable(res, null))

            }else{
                view.setImageResource(R.drawable.usuario)
            }
            cont++
        }*/


        btnLuchar.setOnClickListener {

            if(user!!.vida<=0){
                Toast.makeText(this@Ronda, "No te queda vida ¡Debes curarte para continuar!", Toast.LENGTH_SHORT).show()
            }else{
                val intent: Intent=Intent(this, Batalla::class.java)
                val bundle:Bundle= Bundle()

                bundle.putSerializable("user", user)
                bundle.putSerializable("personaje", personaje)
                bundle.putSerializable("enemigos", enemigos)
                if(partida!=null){
                    bundle.putSerializable("partida",partida)
                }

                intent.putExtras(bundle)
                this.startActivity(intent)
                this.finish()
            }

        }


        btnTienda.setOnClickListener{
            val intent:Intent=Intent(this@Ronda, Tienda::class.java)
            val bundle:Bundle=Bundle()
            bundle.putSerializable("user", user)
            bundle.putSerializable("noDisponibles", noDisponibles)
            bundle.putSerializable("personaje", personaje)
            bundle.putSerializable("enemigos", enemigos)
            if(partida!=null){
                bundle.putSerializable("partida",partida)
            }
            intent.putExtras(bundle)
            this.startActivity(intent)
        }

        btnBeberPocion.setOnClickListener {
            beberPocion()
        }

    }

    /**
     * Función para refrescar los valores de información que se muestran en pantalla.
     * Refresca información de vida, monedas y pociones.
     */
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

    /**
     * Función para incluir la imagen de un personaje en un imageview.
     * Añade un evento onclick para mostrar el nombre del personaje al clickar sobre la imagen.
     * @param personaje Personaje?- personaje que contendrá el nombre de la imagen a utilizar
     * @param view ImageView- ImageView donde se meterá la imagen.
     */
    private fun putImage(personaje: Personaje?, view: ImageView) {

        val imagename:String = personaje!!.foto.substring(0, personaje!!.foto.lastIndexOf("."))
        val res: Int = resources.getIdentifier(imagename, "drawable", packageName)

        if(res!=0){

            view.setImageBitmap(BitmapFactory.decodeResource(resources, res))
        }else{
            view.setImageResource(R.drawable.usuario)
            view.setColorFilter(ContextCompat.getColor(this@Ronda, R.color.blackCM))

        }

        view.setOnClickListener {
            Toast.makeText(this@Ronda, personaje!!.nombre, Toast.LENGTH_SHORT).show()
        }
    }
}