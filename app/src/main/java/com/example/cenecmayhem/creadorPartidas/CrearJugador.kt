package com.example.cenecmayhem.creadorPartidas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
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

    //Botones
    val btnFinalizar:Button by lazy{findViewById(R.id.crJug_btnFinalizar)}
    val btnAñadirAtaque:Button by lazy{findViewById(R.id.crJug_btnAñadirAtaque)}



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

        Log.d("Mau", "Usuario= "+user!!.usuario)
        Log.d("Mau", partida.toString())

        //Preparamos los number picker
        var array= arrayOf("10","20","30","40","50","60","70","80","90")
        fuerzaAtaque.minValue=0
        fuerzaAtaque.maxValue=array.size-1
        fuerzaAtaque.wrapSelectorWheel=false
        fuerzaAtaque.displayedValues=array
        fuerzaAtaque.setOnScrollListener { view, scrollState ->
            probabilidadAtaque.value=100-fuerzaAtaque.value
        }

        probabilidadAtaque.minValue=0
        probabilidadAtaque.maxValue=array.size-1
        probabilidadAtaque.wrapSelectorWheel=false
        probabilidadAtaque.displayedValues=array
        probabilidadAtaque.setOnScrollListener { view, scrollState ->
            fuerzaAtaque.value=100-probabilidadAtaque.value
        }

    }
}