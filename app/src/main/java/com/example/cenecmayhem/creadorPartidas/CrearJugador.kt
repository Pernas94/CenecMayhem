package com.example.cenecmayhem.creadorPartidas

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import clases.Ataque
import clases.Partida
import clases.Personaje
import clases.Usuario
import com.example.cenecmayhem.R
import com.example.cenecmayhem.SeleccionJuego
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CrearJugador : AppCompatActivity() {

    //Bundle
    var user:Usuario?=null
    var partida: Partida? =null

    //Layout
    val numeroPersonajes:TextView by lazy{findViewById(R.id.crJug_contadorPersonajes)}
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
    val btnAñadirPersonaje:Button by lazy{findViewById(R.id.crJug_btnAñadirPersonaje)}

    //Inicializamos el array de ataques y personajes pora rellenarlo más adelante
    var personajes:ArrayList<Personaje> =ArrayList<Personaje>()
    var ataques:ArrayList<Ataque> =ArrayList<Ataque>()

    //BBDD
    val fb: FirebaseFirestore = Firebase.firestore


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

        //Preparamos los numberPickers automatizados y el contador
        setUpPickers()
        numeroPersonajes.text=""+(personajes.size+1)


        btnAñadirAtaque.setOnClickListener {
            añadirAtaque()

            //Si ya se han añadido todos los ataques, bloqueo los demás elementos
            //y muestro el botón de añadir personaje
            if(ataques.size==4){
                marcoAtaques.visibility=View.INVISIBLE
                btnAñadirPersonaje.isEnabled=true
                btnAñadirPersonaje.visibility= View.VISIBLE
            }
        }

        btnAñadirPersonaje.setOnClickListener {
            añadirPersonaje()
            //Una vez añadido el personaje, bloqueo el botón para añadir personajes y
            //muestro la pantalla de creación de ataques
            marcoAtaques.visibility=View.VISIBLE
            btnAñadirPersonaje.isEnabled=false
            btnAñadirPersonaje.visibility= View.GONE
        }

        btnFinalizar.setOnClickListener {

            if(personajes.size>=4){
                //Añado los personajes al objeto Partida
                partida!!.personajes=personajes

                //Recorro los nombres de los personajes para agregarlos como una lista
                var disponibles:List<String> =listOf()
                for (pers in personajes){
                    disponibles=disponibles.plus(pers.nombre)
                }

                val mBuilder= AlertDialog.Builder(this)
                mBuilder.setTitle("Aviso")
                mBuilder.setMessage("A partir de este punto no podrás realizar más cambios a tu partida ni a tus personajes.\n" +
                        "Podrás acceder a ella desde la sección de 'Partidas personalizadas'.\n" +
                        "¿Desea continuar?")
                mBuilder.setPositiveButton("Confirmar", DialogInterface.OnClickListener{
                        dialog, id->

                    val reference=fb.collection("partidas").document(partida!!.nombre)

                    reference.set(
                        hashMapOf(
                            "nombrePartida" to partida!!.nombre,
                            "publica" to partida!!.publica,
                            "creador" to partida!!.creador,
                            "descripcion" to partida!!.descripcion,
                            "disponibles" to disponibles
                        )
                    ).addOnSuccessListener {

                        for (personaje in personajes){
                            reference.collection("personajes").document(personaje.nombre)
                                .set(
                                    hashMapOf(
                                        "precio" to personaje.precio,
                                        "foto" to personaje.foto,
                                        "boss" to false
                                    )
                                ).addOnSuccessListener {

                                    for(ataque in personaje.ataques){
                                        reference.collection("personajes").document(personaje.nombre).collection("ataques").document(ataque.nombre).set(
                                            hashMapOf(
                                                "ataque" to ataque.ataque,
                                                "mensaje" to ataque.mensaje,
                                                "probabilidad" to ataque.probabilidad
                                            )
                                        ).addOnSuccessListener {

                                                val intent: Intent = Intent(this@CrearJugador, SeleccionJuego::class.java)
                                                val bundle:Bundle=Bundle()
                                                bundle.putSerializable("user", user)
                                                intent.putExtras(bundle)
                                                this.startActivity(intent)
                                                this.finish()

                                                Toast.makeText(this, "Paso a seleccion juego", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                        }

                    }



                })

                mBuilder.setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                        dialog, id->
                    dialog.cancel()

                })

                var alert=mBuilder.create()
                alert.show()
            }else{
                Toast.makeText(this@CrearJugador, "¡Asegurate de haber creado al menos 4 personajes!", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Función que borra todos los valores que había previamente en los
     * campos a rellenar del layout. Borra el array de ataques.
     */
    private fun refreshValores() {
        refreshValoresAtaque()
        ataques.clear()
        progressAtaques.progress=ataques.size
        nombrePersonaje.text=""
        precioPersonaje.text=""
        numeroPersonajes.text=""+(personajes.size+1)

    }

    /**
     * Función que limpia los valores introducidos por el usuario en el apartado "Ataques".
     * Limpia el nombre, la fuerza, la probabilidad y el mensaje de ataque.
     */
    private fun refreshValoresAtaque(){
        nombreAtaque.text=""
        fuerzaAtaque.value=4
        probabilidadAtaque.value=4
        mensajeAtaque.text.clear()
    }

    /**
     * Función que, después de comprobar si todos los valores están rellenos, añade un
     * ataque al array de ataques actual.
     */
    private fun añadirAtaque(){

        if(todoRellenoAtaque()){
            //Extraemos los valores de los pickers de ataque
            var fuerzAtaque:Int=Integer.parseInt(fuerzaAtaque.displayedValues.get(fuerzaAtaque.value))
            var probAtaque:Int=Integer.parseInt(probabilidadAtaque.displayedValues.get(probabilidadAtaque.value))

            //Creamos el ataque y lo añadimos al arraylist
            var ataque:Ataque= Ataque(nombreAtaque.text.toString(), fuerzAtaque,probAtaque,mensajeAtaque.text.toString() )
            ataques.add(ataque)
            progressAtaques.progress+=1
            refreshValoresAtaque()

        }else{
            Toast.makeText(this@CrearJugador, "¡Todos los datos de ataque deben estar rellenos!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun añadirPersonaje(){
        var nombre=nombrePersonaje.text.toString()
        var precio=Integer.parseInt(precioPersonaje.text.toString())

        var ataquesAux:ArrayList<Ataque> =ataques.clone() as ArrayList<Ataque>
        //Creo personaje y lo añado al array
        var personaje:Personaje= Personaje(nombre, "", precio, false, ataquesAux)
        Log.d("Mau", "Personaje añadido:   "+personaje.toStringAtaques())
        personajes.add(personaje)

        //Actualizo el contador de personajes
        numeroPersonajes.text=""+(personajes.size+1)
        refreshValores()
        Log.d("Mau", "Ataques del jugador añadido después de limpiar el array==> "+personaje.toStringAtaques())

        //TODO- Comprobar qué pasa con el array de ataques, parece que no se carga nunca.

        //Si ya he agregado el número mínimo de personajes, habilito el botón de finalizar
        if(personajes.size>=4){
            btnFinalizar.isEnabled=true
        }
    }


    /**
     * Función que comprueba que todos los valores de ataque están rellenos.
     * Si falta algún dato, devuelve false.
     * @return Boolean
     */
    private fun todoRellenoAtaque():Boolean{
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
    private fun setUpPickers(){
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