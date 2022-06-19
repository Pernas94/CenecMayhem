package clases

import com.google.gson.Gson
import java.io.Serializable


//RECIBE 4 PARÁMETROS POR DEFECTO PARA PODER SACARLOS DESDE BBDD SIN CARGAR TODA LA INFORMACION
class Personaje(val nombre:String, val foto:String, val precio:Int=1000, val boss:Boolean=false,
                val ataques:ArrayList<Ataque> =ArrayList<Ataque>()):Serializable {

    override fun toString(): String {
        return nombre+", precio="+precio+", foto="+foto+", esBoss="+boss
    }
    fun toStringAtaques():String{
        var texto:String="";
        for (ataque in ataques){
            texto+=ataque.nombre+" | "
        }
        return nombre+"-> Ataques ("+texto+")"
    }

    fun copy():Personaje {
        val JSON = Gson().toJson(this)
        return Gson().fromJson(JSON, Personaje::class.java)
    }

}