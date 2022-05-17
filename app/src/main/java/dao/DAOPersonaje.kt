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
         * Función para guardar en base da datos toda la información pertinente de un personaje
         */
        fun guardarPersonaje(personaje: Personaje){

            fb.collection("personajes").document(personaje.nombre)
                .set(
                    hashMapOf(
                        "precio" to personaje.precio,
                        "foto" to personaje.foto,
                        "desbloqueado" to personaje.desbloqueado
                    )
                )
            guardarAtaques(personaje)

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
                            "mensajeAcierto" to ataque.mensajeAcierto,
                            "mensajeFallo" to ataque.mensajeFallo,
                            "probabilidad" to ataque.probabilidad
                        )
                    )
            }

        }

        fun bajarPersonajes():ArrayList<Personaje>{

            var personajes:ArrayList<Personaje> =ArrayList<Personaje>()
            var ataques:ArrayList<Ataque> =ArrayList<Ataque>()

            fb.collection("personajes").get().addOnSuccessListener {
                    documents->

                for(document in documents){

                    var nombre:String=document.id
                    Log.e("Mau", "Personaje "+nombre.uppercase())
                    //var desbloqueado:Boolean=document.data.get("desbloqueado") as Boolean
                    //var foto:String=document.data.get("foto") as String
                    var precio= document.data.get("precio") as Long
                    Log.e("Mau", "Precio: "+precio)
                    ataques= bajarAtaques(nombre)


                    var personaje:Personaje=Personaje(nombre,"", precio.toInt(), ataques, true)
                    personajes.add(personaje)

                }
            }

            return personajes
        }

        fun bajarAtaques(personaje:String):ArrayList<Ataque>{

            var ataques:ArrayList<Ataque> =ArrayList<Ataque>()

            fb.collection("personajes").document(personaje).collection("ataques").get().addOnSuccessListener {
                    documents->

                for(document in documents){

                    var nombre:String=document.id
                    Log.e("Mau", "    Ataque: "+nombre)
                    var poderAtaque:Long=document.data.get("ataque") as Long
                    var probabilidad:Long=document.data.get("probabilidad") as Long
                    var mensajeAcierto:String=document.data.get("mensajeAcierto").toString()
                    var mensajeFallo:String=document.data.get("mensajeFallo").toString()


                    var ataque:Ataque=Ataque(nombre,poderAtaque.toInt(), probabilidad.toInt(), mensajeAcierto, mensajeFallo)
                    ataques.add(ataque)

                }

            }

            return ataques
        }


    }

}