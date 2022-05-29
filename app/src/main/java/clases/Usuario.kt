package clases

import java.io.Serializable

class Usuario (val email:String, val usuario:String, val vida:Int=100, val dinero:Int=1000,
               val pociones:Int=5, val coronas:Int=0,
               val personajesDisponibles:List<String> = listOf("Pepe1", "Pepe2", "Pepe3", "Pepe4", "Pepe5", "Pepe6"))
    :Serializable{

    override fun toString(): String {
        return email+" ("+usuario+") ->"+ dinero+" - " + pociones+" - "+coronas+" - "+personajesDisponibles
    }
}