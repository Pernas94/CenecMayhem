package com.example.cenecmayhem.creadorPartidas

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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
    val numeroPersonajes:TextView by lazy{findViewById(R.id.crJug_contadorPersonajes)}
    var contadorPersonajes=0
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
        numeroPersonajes.text=""+contadorPersonajes
        contadorPersonajes=3
        numeroPersonajes.text=""+contadorPersonajes






        btnAñadirAtaque.setOnClickListener {
            if(btnAñadirAtaque.text.equals("Añadir personaje")){

                //TODO- Lógica para agregar a BBDD.
                Toast.makeText(this, "Personaje añadido", Toast.LENGTH_SHORT).show()
                cambiarBoton()
                //Actualizo el contador de personajes
                contadorPersonajes+=1
                numeroPersonajes.text=""+contadorPersonajes
                refreshValores()

                //Si ya he agregado el número mínimo de personajes, habilito el botón de finalizar
                if(contadorPersonajes>=4){
                    btnFinalizar.isEnabled=true
                }

            }else{
                añadirAtaque()
                //Si ya se han añadido todos los ataques, bloqueo los demás elementos
                if(ataques.size==4){
                    cambiarBoton()
                }
            }

        }


        btnFinalizar.setOnClickListener {

        }

        var ataque:Ataque=Ataque("atk", 20,80,"sadadas","")
        var ataque1:Ataque=Ataque("atk", 20,80,"sadadas","")
        var ataque2:Ataque=Ataque("atk", 20,80,"sadadas","")
        ataques.add(ataque)
        ataques.add(ataque1)
        ataques.add(ataque2)
        progressAtaques.progress=contadorPersonajes

    }

    /**
     * Función que borra todos los valores que había previamente en los
     * campos a rellenar del layout
     */
    private fun refreshValores() {
        ataques.clear()
        progressAtaques.progress=0
        nombrePersonaje.text=""
        precioPersonaje.text=""
        nombreAtaque.text=""
        fuerzaAtaque.value=4
        probabilidadAtaque.value=4
        mensajeAtaque.text.clear()

    }

    fun añadirAtaque(){

        if(todoRellenoAtaque()){
            //Extraemos los valores de los pickers de ataque
            var fuerzAtaque:Int=Integer.parseInt(fuerzaAtaque.displayedValues.get(fuerzaAtaque.value))
            var probAtaque:Int=Integer.parseInt(probabilidadAtaque.displayedValues.get(probabilidadAtaque.value))

            //Creamos el ataque y lo añadimos al arraylist
            var ataque:Ataque= Ataque(nombreAtaque.text.toString(), fuerzAtaque,probAtaque,mensajeAtaque.text.toString(),"" )
            ataques.add(ataque)
            progressAtaques.progress+=1



        }else{
            Toast.makeText(this@CrearJugador, "¡Todos los datos deben estar rellenos!", Toast.LENGTH_SHORT).show()
        }

    }

    fun añadirPersonaje(){

    }

    fun cambiarBoton(){

        //Bloqueo/desbloque los botones
        nombreAtaque.isEnabled=!nombreAtaque.isEnabled
        fuerzaAtaque.isEnabled=!fuerzaAtaque.isEnabled
        probabilidadAtaque.isEnabled=!probabilidadAtaque.isEnabled
        mensajeAtaque.isEnabled=!mensajeAtaque.isEnabled

        //Adapto los colores y el tamaño de la fuente a cada caso
        var colorTexto: Int? = null
        var colorFondo: Int? =null
        if(btnAñadirAtaque.text.equals("Añadir personaje")){
            btnAñadirAtaque.text="Añadir ataque"
            btnAñadirAtaque.textSize=18f
            colorTexto=getColor(R.color.blackCM)
            colorFondo=resources.getColor(R.color.yellowCM)


        }else{
            btnAñadirAtaque.text="Añadir personaje"
            btnAñadirAtaque.textSize=15f
            colorTexto=getColor(R.color.whiteCM)
            colorFondo=resources.getColor(R.color.redCM)
        }

        btnAñadirAtaque.setTextColor(colorTexto!!)
        btnAñadirAtaque.backgroundTintList = ColorStateList.valueOf(colorFondo!!)
    }

    /**
     * Función que comprueba si se han rellenado todos los campos y si se han agregado
     * al menos 4 personajes creados por el usuario
     * @return Boolean- bool de comprobación. Devuelve false se no se cumple alguno de los requisitos
     */
    fun completado():Boolean{
        var relleno:Boolean=true
        //Comprobamos que no están vacíos los campos de
        if(nombrePersonaje.text.isNullOrEmpty()||precioPersonaje.text.isNullOrEmpty()|| ataques.size<4){
            relleno=false
        }
        return relleno
    }

    fun todoRellenoAtaque():Boolean{
        var relleno:Boolean=true
        if(nombrePersonaje.text.isNullOrEmpty()||precioPersonaje.text.isNullOrEmpty()||nombreAtaque.text.isNullOrEmpty()||mensajeAtaque.text.isNullOrEmpty()){
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