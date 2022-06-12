package clases

import java.io.Serializable

class Partida (var nombre:String, var creador:String,
               var publica:Boolean=true, var descripcion:String="", var personajes:ArrayList<Personaje> =ArrayList<Personaje>()):
    Serializable {

    override fun toString(): String {
        return nombre+": Creador="+creador+", Publica="+publica+", NumPersonajes="+personajes.size+", Descripcion="+descripcion
    }
}