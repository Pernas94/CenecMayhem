package dao

import android.util.Log
import android.widget.ArrayAdapter
import clases.Ataque
import clases.Personaje
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DAOPersonaje {
    companion object{

        val fb:FirebaseFirestore=Firebase.firestore


        /**
         * Función para guardar en base da datos toda la información pertinente de un personaje.
         * Llama a la función guardarAtaques()
         */
        fun guardarPersonaje(personaje: Personaje){

            fb.collection("personajes").document(personaje.nombre)
                .set(
                    hashMapOf(
                        "precio" to personaje.precio,
                        "foto" to personaje.foto,
                        "boss" to false
                    )
                )
            //guardarAtaques(personaje)
        }

        /**
         * Función para guardar los ataques de un personaje en base de datos.
         * Utilizada a la par de guardarPersonaje()
         */
        private fun guardarAtaques(personaje:Personaje){

            for (ataque in personaje.ataques){
                fb.collection("personajes").document(personaje.nombre).collection("ataques")
                    .document(ataque.nombre).set(
                        hashMapOf(
                            "ataque" to ataque.ataque,
                            "probabilidad" to ataque.probabilidad,
                            "mensaje" to ataque.mensaje

                        )
                    )
            }
        }

        /**
         * Baja toda la información de un personaje de la BBDD
         */
        fun bajarPersonajeCompleto(idPersonaje:String):Personaje?{

            var personaje:Personaje?=null
            var ataques:ArrayList<Ataque> =ArrayList<Ataque>()

            fb.collection("personajes").document(idPersonaje).get().addOnSuccessListener {

                var nombre:String=it.id
                Log.e("Mau", "Personaje "+nombre.uppercase())
                var foto:String=it.data?.get("foto") as String
                var boss:Boolean=it.data?.get("boss") as Boolean
                var precio= it.data?.get("precio") as Long
                ataques= bajarAtaques(nombre)
                personaje=Personaje(nombre, foto, precio.toInt(), boss, ataques)
                Log.e("Mau", "\t"+personaje.toString())
            }
            return personaje
        }

        /**
         * Función para bajar ataques de un personaje en concreto.
         */
        private fun bajarAtaques(idPersonaje:String):ArrayList<Ataque>{

            var ataques:ArrayList<Ataque> =ArrayList<Ataque>()

            fb.collection("personajes").document(idPersonaje).collection("ataques").get().addOnSuccessListener {
                    documents->

                for(document in documents){

                    var nombre:String=document.id
                    var poderAtaque:Long=document.data.get("ataque") as Long
                    var probabilidad:Long=document.data.get("probabilidad") as Long
                    var mensajeAcierto:String=document.data.get("mensajeAcierto").toString()
                    var mensajeFallo:String=document.data.get("mensajeFallo").toString()


                    var ataque:Ataque=Ataque(nombre,poderAtaque.toInt(), probabilidad.toInt(), mensajeAcierto)
                    ataques.add(ataque)

                }

            }

            return ataques
        }

        /**
         * Función para bajar todos los personajes SIMPLES (solo información de nombre, foto y desbloqueado) para
         * cargar en el RecyclerView de SeleccionPersonaje.
         */
        fun bajarTodosPersonajes():ArrayList<Personaje>{
            val personajes:ArrayList<Personaje> =ArrayList<Personaje>()

            fb.collection("personajes").get().addOnSuccessListener {
                    documents->

                for(document in documents){

                    var nombre:String=document.id
                    Log.e("Mau", "Personaje "+nombre.uppercase())
                    var desbloqueado:Boolean=document.data.get("desbloqueado") as Boolean
                    var foto:String=document.data.get("foto") as String
                    Log.e("Mau", "\t"+desbloqueado+" "+foto)

                    var personaje:Personaje=Personaje(nombre,foto)
                    personajes.add(personaje)

                }
            }

            //https://stackoverflow.com/questions/30659569/wait-until-firebase-retrieves-data
            //Info sobre como frenar el programa para esperar a la BBDD
            return personajes
        }


    }

}