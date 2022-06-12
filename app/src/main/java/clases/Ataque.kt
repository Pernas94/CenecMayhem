package clases

import java.io.Serializable

class Ataque(val nombre:String, val ataque:Int, val probabilidad:Int, val mensajeAcierto:String, val mensajeFallo:String):
    Serializable {

}