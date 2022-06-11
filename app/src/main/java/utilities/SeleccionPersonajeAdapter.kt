package utilities

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import clases.Ataque
import clases.Personaje
import clases.Usuario
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.cenecmayhem.R
import com.example.cenecmayhem.Ronda
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dao.DAOPersonaje
import java.io.File
import java.io.Serializable

class SeleccionPersonajeAdapter(val contexto: Activity, val personajes: ArrayList<Personaje>,
                                val posiblesEnemigos:ArrayList<Personaje>, val personajesNoDisponibles:ArrayList<Personaje>,
                                val user: Usuario?) :
    RecyclerView.Adapter<SeleccionPersonajeAdapter.ViewHolder>() {


    val storage: StorageReference = Firebase.storage.reference


        /**
     * Función para inflar el layout deseado en el viewgroup deseado
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        val v =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.card_personaje, viewGroup, false)
        return ViewHolder(v)
    }


    /**
     * Función que carga los elementos en el viewHolder y asigna los eventos onclick a diferentes elementos del layout.
     * Estos botones permiten ir a juegoAmpliado con un juego concreto o eliminar el juego de la biblioteca del usuario.
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {


        val personaje: Personaje = personajes.get(i)

        //Extraemos la imagen del personaje de Firebase Storage
        val path="cenec/"+personaje.foto
        storage.child(path)
        val extension: String = personaje.foto.substring(personaje.foto.lastIndexOf('.') + 1)
        val localfile = File.createTempFile("tempImage", extension)

        //TODO- Solucionar problema de carga de imagenes
        storage.getFile(localfile).addOnSuccessListener {

            val uri:Uri=Uri.parse(localfile.path)
            //viewHolder.foto.setImageURI(null)
            //viewHolder.foto.setImageURI(uri)
            val bitmap = BitmapFactory.decodeFile(localfile.path)
            viewHolder.foto.setImageBitmap(bitmap)


        }.addOnFailureListener {

            it.printStackTrace()
            //val uri: Uri = Uri.parse("./app/src/main/res/drawable/alexok.png")
            viewHolder.foto.setImageResource(R.drawable.usuario)
        }


        viewHolder.nombre.text = personaje.nombre.uppercase()
        viewHolder.marco.setOnClickListener {

            val mBuilder= AlertDialog.Builder(contexto)
            mBuilder.setTitle("Confirmar personaje")
            mBuilder.setMessage("¿Desea jugar como "+personaje.nombre+"?")
            mBuilder.setPositiveButton("Confirmar", DialogInterface.OnClickListener{
                    dialog, id->

                if(posiblesEnemigos.contains(personaje)) {
                    //Si el personaje escogido está en el arrayList de enemigos,lo borro
                    posiblesEnemigos.remove(personaje)
                }else{
                    //Si el personaje escogido no está en el arraylist de enemigos, borro el primer enemigo
                    posiblesEnemigos.removeAt(0)
                }


                //Pasamos el usuario, el personaje y los enemigos a la siguiente pantalla, de Ronda.
                val intent:Intent= Intent(contexto, Ronda::class.java)
                val bundle: Bundle =Bundle()
                bundle.putSerializable("user", user)
                bundle.putSerializable("personaje", personaje)
                bundle.putSerializable("enemigos", posiblesEnemigos)
                bundle.putSerializable("noDisponibles", personajesNoDisponibles)
                intent.putExtras(bundle)
                contexto.startActivity(intent)
            })

            mBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                dialog, id->
            dialog.cancel()

        })

            var alert=mBuilder.create()
            alert.show()
        }
    }


    override fun getItemCount(): Int {
        return personajes.size
    }


    /**
     * Clase interna con el viewHolder, que contiene los elementos que se van a cargar y los relaciona con el layout
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var foto: ImageView
        var nombre: TextView
        var marco: ConstraintLayout

        init {
            foto = itemView.findViewById(R.id.card_foto)
            nombre = itemView.findViewById(R.id.card_nombre)
            marco = itemView.findViewById(R.id.card_marco)
        }
    }
}