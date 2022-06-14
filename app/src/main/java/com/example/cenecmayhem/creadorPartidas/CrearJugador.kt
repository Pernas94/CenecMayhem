package com.example.cenecmayhem.creadorPartidas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import clases.Ataque
import clases.Partida
import clases.Usuario
import com.example.cenecmayhem.R

class CrearJugador : AppCompatActivity() {

    //Bundle
    var user:Usuario?=null
    var partida: Partida? =null

    //Layout
    val contadorPersonaje:TextView by lazy{findViewById(R.id.crJug_contadorPersonajes)}
    val fotoPersonaje:ImageView by lazy{findViewById(R.id.crJug_imagen)}
    val nombrePersonaje:TextView by lazy{findViewById(R.id.crJug_nombrePersonaje)}
    val precioPersonaje:TextView by lazy{findViewById(R.id.crJug_precioPersonaje)}
    val progressAtaques:ProgressBar by lazy{findViewById(R.id.crJug_progressAtaques)}
    val nombreAtaque:TextView by lazy{findViewById(R.id.crJug_nombreAtaque)}
    val fuerzaAtaque: NumberPicker by lazy{findViewById(R.id.crJug_pickerFuerza)}
    val probabilidadAtaque:NumberPicker by lazy{findViewById(R.id.crJug_pickerProb)}
    val mensajeAtaque:EditText by lazy{findViewById(R.id.crJug_mensajeAtaque)}
    val marcoAtaques:ConstraintLayout by lazy{findViewById(R.id.crJug_marcoAtaques)}

    //Botones
    val btnFinalizar:Button by lazy{findViewById(R.id.crJug_btnFinalizar)}
    val btnAñadirAtaque:Button by lazy{findViewById(R.id.crJug_btnAñadirAtaque)}

    //Inicializamos el array de ataques pora rellenarlo más adelante
    var ataques:ArrayList<Ataque> =ArrayList<Ataque>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_jugador)

        val userInfo = intent.extras
        if (userInfo != null) {
            if (userInfo.getSerializable("user") != null) {
                user = userInfo.getSerializable("user") as Usuario
            }

            if (userInfo.getSerializable("partida")!=null){
                partida=userInfo.getSerializable("partida") as Partida
            }
        }

        //Preparamos los numberPickers automatizados
        setUpPickers()

        btnAñadirAtaque.setOnClickListener {
            añadirAtaque()
        }

        btnFinalizar.isEnabled=true
        btnFinalizar.setOnClickListener {
            var personajeAñadido=false

            if(personajeAñadido){
                btnAñadirAtaque.isVisible=true
                val inflater: LayoutInflater =this.layoutInflater
                val vg=inflater.inflate(R.id.crJug_marcoAtaques, findViewById(R.id.crJug_marcoAtaques))
            }else{
                btnAñadirAtaque.isVisible=false
                val inflater: LayoutInflater =this.layoutInflater
                val vg=inflater.inflate(R.layout.infl_crear_jugador, findViewById(R.id.crJug_marcoAtaques))
                val btnAñadirPersonaje:Button = vg.findViewById(R.id.crJug_btnAñadirPersonaje)
                btnAñadirPersonaje.setOnClickListener {
                    Toast.makeText(this@CrearJugador, "Añado personaje", Toast.LENGTH_SHORT).show()
                    personajeAñadido=!personajeAñadido
                }
            }


        }




    }

    fun añadirAtaque(){

        if(todoRellenoAtaque()){
            //Extraemos los valores de los pickers de ataque
            var fuerzAtaque:Int=Integer.parseInt(fuerzaAtaque.displayedValues.get(fuerzaAtaque.value))
            var probAtaque:Int=Integer.parseInt(probabilidadAtaque.displayedValues.get(probabilidadAtaque.value))

            //Creamos el ataque y lo añadimos al arraylist
            var ataque:Ataque= Ataque(nombreAtaque.text.toString(), fuerzAtaque,probAtaque,mensajeAtaque.text.toString(),"" )
            ataques.add(ataque)

            //Si ya se han añadido todos los ataques, bloqueo los demás elementos
            if(ataques.size==4){
                val inflater: LayoutInflater =this.layoutInflater
                inflater.inflate(R.layout.infl_crear_jugador, findViewById(R.id.crJug_marcoAtaques))
            }

        }else{
            Toast.makeText(this@CrearJugador, "¡Todos los datos del ataque deben estar rellenos!", Toast.LENGTH_SHORT).show()
        }

    }

    fun añadirPersonaje(){

    }

    fun todoRelleno():Boolean{
        var relleno:Boolean=true
        //Comprobamos que no están vacíos los campos de
        if(nombrePersonaje.text.isNullOrEmpty()||precioPersonaje.text.isNullOrEmpty()|| ataques.size<4){
            relleno=false
        }
        return relleno
    }

    fun todoRellenoAtaque():Boolean{
        var relleno:Boolean=true
        if(nombreAtaque.text.isNullOrEmpty()||mensajeAtaque.text.isNullOrEmpty()){
            relleno=false
        }
        return relleno
    }

    /**
     * Función que prepara los Number Pickers del layout para desplegar unos datos concretos,
     * y hacer que una depende de la otra. El valor escogido en el numberPicker A afecta directamente
     * al numberPicker B. Los valores mostrados en los pickers siempre sumaran 100.
     */
    fun setUpPickers(){
        //Preparamos los number picker
        var array= arrayOf("10","20","30","40","50","60","70","80","90")

        fuerzaAtaque.minValue=0
        fuerzaAtaque.maxValue=array.size-1
        fuerzaAtaque.wrapSelectorWheel=false
        fuerzaAtaque.displayedValues=array
        fuerzaAtaque.value=4
        fuerzaAtaque.setOnScrollListener { view, scrollState ->
            if(scrollState==0){
                probabilidadAtaque.value=(array.size-1)-fuerzaAtaque.value
            }
        }

        probabilidadAtaque.minValue=0
        probabilidadAtaque.maxValue=array.size-1
        probabilidadAtaque.wrapSelectorWheel=false
        probabilidadAtaque.displayedValues=array
        probabilidadAtaque.value=4
        probabilidadAtaque.setOnScrollListener { view, scrollState ->
            if(scrollState==0){
                fuerzaAtaque.value=(array.size-1)-probabilidadAtaque.value
            }
        }
    }
}