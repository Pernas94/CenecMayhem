package com.example.cenecmayhem.cenecMayhem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import clases.Personaje
import clases.Usuario
import com.example.cenecmayhem.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import utilities.SeleccionPersonajeAdapter

class SeleccionPersonaje : AppCompatActivity() {

    var user: Usuario? = null
    val fb: FirebaseFirestore = Firebase.firestore
    val txtNombreUsuario: TextView by lazy { findViewById(R.id.selPers_txtUsername) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_personaje)

        val recycler: RecyclerView = findViewById(R.id.selPer_recyclerPersonajes)

        val userInfo = intent.extras
        if (userInfo != null) {
            if (userInfo.getSerializable("user") != null) {
                user = userInfo.getSerializable("user") as Usuario?

                txtNombreUsuario.text = user?.usuario
            }
        }


        //Saco los personajes disponibles del usuario
        val disponibles = user?.personajesDisponibles


        if (disponibles != null) {
            //Array donde se agregarán los personajes disponibles para el usuario
            val arrayDisponibles: ArrayList<Personaje> = ArrayList<Personaje>()

            //Array donde se agregaran cuatro personajes aleatorios, 3 de los cuales serán enemigos. El 4 se borrará.
            val arrayPosiblesEnemigos: ArrayList<Personaje> = ArrayList<Personaje>();

            //Array donde se agregarán los personajes no disponibles del usuario, para pasarlos a la tienda.
            val arrayNoDisponibles:ArrayList<Personaje> =ArrayList<Personaje>()

            //Recorremos TODOS los personajes de la base de datos
            //De esto se obtendrán los personajes disponibles para el usuario y una lista de enemigos aleatoria
            val docRef = fb.collection("personajes")
            docRef.get().addOnSuccessListener { documents ->


                var length = documents.size()
                val arrayRandoms: ArrayList<Int> = getRandoms(length);
                var contador: Int = 0

                for (doc in documents) {

                    val nombre: String = doc.id
                    val foto: String = doc.data?.get("foto") as String
                    val precio: Int = (doc.data?.get("precio") as Long).toInt()
                    val boss: Boolean = doc.data?.get("boss") as Boolean
                    val personaje: Personaje = Personaje(nombre, foto, precio, boss)

                    //Si el contador conincide con uno de los aleatorios, se agrega el personaje a posibles enemigos
                    if (arrayRandoms.contains(contador)) {
                        Log.d("Mau", "Contador="+contador+", Agrego al personaje "+personaje.nombre)
                        arrayPosiblesEnemigos.add(personaje)
                    }

                    //Se comprueba si el personaje está entre los personajes desbloqueados del usuario
                    if (disponibles.contains(doc.id)) {
                        arrayDisponibles.add(personaje)
                    }else{
                        arrayNoDisponibles.add(personaje)
                    }

                    contador++;
                }

                if (arrayDisponibles.size > 0) {
                    val adapter = SeleccionPersonajeAdapter(
                        this,
                        arrayDisponibles,
                        arrayPosiblesEnemigos,
                        arrayNoDisponibles,
                        user
                    )
                    recycler.layoutManager = GridLayoutManager(this@SeleccionPersonaje, 2)
                    recycler.adapter = adapter
                } else {
                    Log.d("Mau", "No se encontraron personajes para cargar")
                }


            }
                .addOnFailureListener { exception ->
                   Log.d("Mau", exception.toString())
                }

        } else {
            Toast.makeText(
                this@SeleccionPersonaje,
                "El usuario no tiene personajes para mostrar",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Función que genera 4 números aleatorios diferentes entre 0 y el parámetro Length
     * @param length Int- tamaño máxima del aleatorio
     * @return ArrayList<Int>- ArrayList con los cuatro valores aleatorios
     */
    private fun getRandoms(length: Int): ArrayList<Int> {
        var array = ArrayList<Int>();

        while (array.size < 4) {
            var random = (0..length-1).random()
            //Si el número no está en el array, se agrega
            if (!array.contains(random)) array.add(random)
        }
        Log.e("Mau", "Longitud="+length+"    Randoms="+array[0]+" "+array[1]+" "+array[2]+" "+array[3])
        return array;
    }
}