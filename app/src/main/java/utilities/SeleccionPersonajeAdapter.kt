package utilities

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import clases.Personaje
import com.example.cenecmayhem.R

class SeleccionPersonajeAdapter(val contexto: Activity, val personajes: ArrayList<Personaje>): RecyclerView.Adapter<SeleccionPersonajeAdapter.ViewHolder>() {


    /**
     * Función para inflar el layout deseado en el viewgroup deseado
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.card_personaje, viewGroup, false)
        return ViewHolder(v)
    }


    /**
     * Función que carga los elementos en el viewHolder y asigna los eventos onclick a diferentes elementos del layout.
     * Estos botones permiten ir a juegoAmpliado con un juego concreto o eliminar el juego de la biblioteca del usuario.
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        //Escojo un elemento del array para cargar toda la información. Vendría de BBDD
        var personaje: Personaje = personajes.get(i)

        viewHolder.nombre.text = personaje.nombre

        //SOLUCION TEMPORAL PARA LAS IMAGENES
        val uri:Uri=Uri.parse("app/src/main/res/drawable/logo.png")
        viewHolder.foto.setImageURI(uri)
        viewHolder.marco.setOnClickListener {
            Toast.makeText(contexto, "Has elegido a "+personaje.nombre, Toast.LENGTH_LONG).show()

        }

    }

    override fun getItemCount(): Int {
        return personajes.size
    }



    /**
     * Clase interna con el viewHolder, que contiene los elementos que se van a cargar y los relaciona con el layout
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var foto:ImageView
        var nombre:TextView
        var marco:ConstraintLayout

        init {
            foto=itemView.findViewById(R.id.card_foto)
            nombre=itemView.findViewById(R.id.card_nombre)
            marco=itemView.findViewById(R.id.card_marco)
        }

    }

}