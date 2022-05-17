package clases

import java.io.Serializable

//foto es String por prueba, será otro tipo de dato
class Personaje(val nombre:String, val foto:String, val precio:Int, val ataques:ArrayList<Ataque>, val desbloqueado:Boolean):Serializable {

}