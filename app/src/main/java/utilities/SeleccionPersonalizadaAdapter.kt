package utilities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import clases.Partida
import clases.Personaje
import clases.Usuario
import com.example.cenecmayhem.R
import com.example.cenecmayhem.cenecMayhem.SeleccionPersonaje
import dao.DAOAuth

class SeleccionPersonalizadaAdapter (val contexto: Activity, var user: Usuario?,
var partidas:ArrayList<Partida>):
    RecyclerView.Adapter<SeleccionPersonalizadaAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SeleccionPersonalizadaAdapter.ViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.card_partida_personalizada, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: SeleccionPersonalizadaAdapter.ViewHolder, i: Int) {

        viewHolder.creador.text=partidas.get(i).creador
        viewHolder.descripcionPartida.text=partidas.get(i).descripcion
        viewHolder.nombrePartida.text=partidas.get(i).nombre

        viewHolder.marco.setOnClickListener {

            val intent: Intent =Intent(contexto, SeleccionPersonaje::class.java)
            var bundle:Bundle= Bundle()
            bundle.putSerializable("user", user)
            bundle.putSerializable("partida", partidas.get(i))
            intent.putExtras(bundle)
            contexto.startActivity(intent)


            Toast.makeText(contexto, "Vas a jugar a "+partidas.get(i).nombre, Toast.LENGTH_SHORT).show()
        }

    }


    override fun getItemCount(): Int {
        return partidas.size
    }


    /**
     * Clase interna con el viewHolder, que contiene los elementos que se van a cargar y los relaciona con el layout
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nombrePartida:TextView
        var descripcionPartida:TextView
        var creador:TextView
        var marco:ConstraintLayout

        init {
            nombrePartida=itemView.findViewById(R.id.card_nombrePartida)
            descripcionPartida=itemView.findViewById(R.id.card_descripcionPartida)
            creador=itemView.findViewById(R.id.card_creador)
            marco=itemView.findViewById(R.id.card_marcoPartida)
        }
    }
}