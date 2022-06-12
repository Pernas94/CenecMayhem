package utilities

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import clases.Personaje
import clases.Usuario
import com.example.cenecmayhem.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dao.DAOAuth

class TiendaAdapter(val contexto: Activity, val noDisponibles:ArrayList<Personaje>, var txtDinero:TextView,
                    var user: Usuario?):
    RecyclerView.Adapter<TiendaAdapter.ViewHolder>() {

    val fb: FirebaseFirestore = Firebase.firestore

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.card_comprable, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        var personaje:Personaje=noDisponibles.get(i)
        viewHolder.nombre.text=personaje.nombre
        viewHolder.precio.text=personaje.precio.toString()


        viewHolder.btnComprar.setOnClickListener {

            //Si tiene dinero suficiente, se compra el personaje, se añade al array de disponibles, y se borra del recycler
            if(user!!.dinero>personaje.precio){

                //Update valores de user y del layout
                user!!.dinero-=personaje.precio
                user!!.personajesDisponibles=user!!.personajesDisponibles.plus(personaje.nombre)
                txtDinero.text=""+user!!.dinero

                //Borro los valores de la colección no disponible y aplico cambio a recycler
                Toast.makeText(contexto, "¡Has adquirido a "+personaje.nombre+"!", Toast.LENGTH_SHORT).show()
                noDisponibles.remove(personaje)
                notifyDataSetChanged();

                //Update BBDD
                DAOAuth.updateUserInfo(user)

            }else{
                Toast.makeText(contexto, "¡No tienes suficiente dinero!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun getItemCount(): Int {
        return noDisponibles.size
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