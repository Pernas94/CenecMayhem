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
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import clases.Partida
import clases.Personaje
import clases.Usuario
import com.example.cenecmayhem.R
import com.example.cenecmayhem.Ronda
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File


class SeleccionPersonajeAdapter(val contexto: Activity, val personajes: ArrayList<Personaje>,
                                val posiblesEnemigos:ArrayList<Personaje>, val personajesNoDisponibles:ArrayList<Personaje>,
                                val partida: Partida?, val user: Usuario?) :
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


        if(partida!=null){

            viewHolder.foto.setImageResource(R.drawable.usuario)
        }else{
            val imagename:String = personaje.foto.substring(0, personaje.foto.lastIndexOf("."))
            val res: Int = contexto.resources.getIdentifier(imagename, "drawable", contexto.packageName)
            viewHolder.foto.setImageResource(res)
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
                if(partida!=null){
                    bundle.putSerializable("partida", partida)
                }
                intent.putExtras(bundle)
                contexto.startActivity(intent)
                contexto.finish()
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