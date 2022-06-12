package dao

import android.util.Log
import clases.Partida
import clases.Personaje
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DAOPartida {
    companion object{
        val fb: FirebaseFirestore = Firebase.firestore


        /**
         * Función para cargar una información de partida a BBDD.
         * @param partida Partida- Objeto de tipo Partida cuya información será cargada.
         */
        fun guardarPartida(partida: Partida){

            fb.collection("partidas").document(partida.nombre)
                .set(
                    hashMapOf(
                        "nombrePartida" to partida.nombre,
                        "publica" to partida.publica,
                        "creador" to partida.creador,
                        "descripcion" to partida.descripcion

                    )
                ).addOnSuccessListener {
                    Log.d("Mau", "Añadida partida "+partida.nombre.uppercase())
                    var array= partida.personajes

                    for (personaje in array){
                        guardarPersonajePartida(personaje, partida)
                    }
                }.addOnFailureListener {
                    Log.d("Mau", "No se ha cargado la partida "+partida.nombre.uppercase())
                    it.printStackTrace()
                }


        }



        /**
         * Función para guardar en base da datos toda la información pertinente de un personaje.
         * Llama a la función guardarAtaques()
         */
        fun guardarPersonajePartida(personaje: Personaje, partida:Partida){

            fb.collection("partidas").document(partida.nombre).collection("personajes").document(personaje.nombre)
                .set(
                    hashMapOf(
                        "precio" to personaje.precio,
                        "foto" to personaje.foto,
                        "boss" to false
                    )
                ).addOnSuccessListener {
                    guardarAtaques(personaje, partida)
                }

        }



        /**
         * Función para guardar los ataques de un personaje en base de datos.
         * Utilizada a la par de guardarPersonaje()
         */
        private fun guardarAtaques(personaje:Personaje, partida:Partida){

            for (ataque in personaje.ataques){
                fb.collection("partidas").document(partida.nombre).collection("personajes").document(personaje.nombre).collection("ataques").document(ataque.nombre).set(
                        hashMapOf(
                            "ataque" to ataque.ataque,
                            "mensajeAcierto" to ataque.mensajeAcierto,
                            "mensajeFallo" to ataque.mensajeFallo,
                            "probabilidad" to ataque.probabilidad
                        )
                    )
            }
        }











    }
}