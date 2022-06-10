package utilities

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import clases.Personaje
import com.example.cenecmayhem.R
import com.google.firebase.firestore.FirebaseFirestore

class TiendaAdapter(val contexto: Activity, val personajesBloqueados:ArrayList<Personaje>): RecyclerView.Adapter<TiendaAdapter.ViewHolder>() {



    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.card_comprable, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {




    }

    override fun getItemCount(): Int {
        return personajesBloqueados.size
    }



    /**
     * Clase interna con el viewHolder, que contiene los elementos que se van a cargar y los relaciona con el layout
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nombre:TextView
        var precio:TextView
        var btnComprar:ImageButton

        init {
            nombre=itemView.findViewById(R.id.comp_nombreItem)
            precio=itemView.findViewById(R.id.comp_precioItem)
            btnComprar=itemView.findViewById(R.id.comp_btnComprar)
        }
    }



}