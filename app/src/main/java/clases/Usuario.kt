package clases

import java.io.Serializable

class Usuario (val email:String, val usuario:String, var vida:Int=100, var dinero:Int=1000,
               var pociones:Int=5, var coronas:Int=0,
               var personajesDisponibles:List<String> = listOf("Pepe1", "Pepe2", "Pepe3", "Pepe4", "Pepe5", "Pepe6"))
    :Serializable{

    override fun toString(): String {
        return email+" ("+usuario+") ->"+ dinero+" - " + pociones+" - "+coronas+" - "+personajesDisponibles
    }
}