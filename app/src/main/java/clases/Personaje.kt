package clases

import java.io.Serializable

//RECIBE 4 PARÁMETROS POR DEFECTO PARA PODER SACARLOS DESDE BBDD SIN CARGAR TODA LA INFORMACION
class Personaje(val nombre:String, val foto:String, val precio:Int=1000, boss:Boolean=false,
                val ataques:ArrayList<Ataque> =ArrayList<Ataque>()):Serializable {

}