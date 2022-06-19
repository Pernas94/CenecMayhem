package com.example.cenecmayhem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import clases.Ataque
import clases.Partida
import clases.Personaje
import clases.Usuario
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dao.DAOPartida
import dao.DAOPersonaje

class MainActivity : AppCompatActivity() {

    val irARegistro: Button by lazy{findViewById(R.id.button2)}
    val irALogin:Button by lazy{findViewById(R.id.btnLogin)}
    val irASeleccionarPartida:Button by lazy{findViewById(R.id.btnSeleccionarPartida)}
    val btnAux:Button by lazy{findViewById(R.id.btnAux)}

    val fb: FirebaseFirestore =Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        irARegistro.setOnClickListener {
            val intent: Intent =Intent(this, Registro::class.java)
            this.startActivity(intent)
        }

        irALogin.setOnClickListener {
            val intent:Intent=Intent(this, Login::class.java)
            this.startActivity(intent)
        }

        irASeleccionarPartida.setOnClickListener {
            val intent:Intent=Intent(this, SeleccionJuego::class.java)
            val bundle:Bundle=Bundle()
            val user:Usuario= Usuario("pepe@a.es", "", 1000, 5, 0)
            bundle.putSerializable("user", user)
            intent.putExtras(bundle)
            this.startActivity(intent)
        }

        btnAux.setOnClickListener {

            crearCenecMayhem()
        }


    }



    fun crearCenecMayhem(){
        var precioNormal:Int=5000
        var precioBoss:Int=15000

        var ataqueAlex1:Ataque=Ataque("Amo a vé", 70,30, "encoge los hombros y dice 'pero amo a vé'.")
        var ataqueAlex2:Ataque=Ataque("Acentaco", 50,50, "comienza a hablar en un incomprensible dialecto cordobés, para marear al enemigo.")
        var ataqueAlex3:Ataque=Ataque("Aliento", 30,70, "se pimpla tres Alientos de Dragón y se pone agresivo. Quiere zurrar a alguien...")
        var ataqueAlex4:Ataque=Ataque("Scooter", 40,60, "arranca el patinete eléctrico a toda hostia para intentar atropellar al enemigo.")
        var ataquesAlex= ArrayList<Ataque>()
        ataquesAlex.add(ataqueAlex1)
        ataquesAlex.add(ataqueAlex2)
        ataquesAlex.add(ataqueAlex3)
        ataquesAlex.add(ataqueAlex4)
        var alex:Personaje=Personaje("Alex", "cm_alex.png", precioNormal, false, ataquesAlex)

        var ataqueAntonio1:Ataque= Ataque("Fast hands", 50,50, "comienza a teclear a velocidades sobrehumanas para fundir el cerebro del enemigo.")
        var ataqueAntonio2: Ataque=Ataque("PokeMan", 40,60, "se pone su sudadera de Umbreon y se esconda en las sombras para atacar sin ser visto.")
        var ataqueAntonio3= Ataque("Sugarrr", 70,30, "intenta sepultar al enemigo bajo toneladas de azúcar. El equivalente a lo que echa al colacao una mañána cualquiera.")
        var ataqueAntonio4= Ataque("Dibu", 60,40, "avisa a su colega diseñador para que le pinte la puta cara al enemigo.")
        var ataquesAntonio=ArrayList<Ataque>()
        ataquesAntonio.add(ataqueAntonio1)
        ataquesAntonio.add(ataqueAntonio2)
        ataquesAntonio.add(ataqueAntonio3)
        ataquesAntonio.add(ataqueAntonio4)
        var antonio=Personaje("Lander", "cm_antonio.png", precioNormal, false, ataquesAntonio)

        var ataqueRoberto1=Ataque("Poliamor", 60,40, "comienza a dar la chapa al enemigo con las ventajas del poliamor. Acto seguido intenta menterle boca.")
        var ataqueRoberto2=Ataque("Ambush", 50,50, "pide al enemigo que le pinte 'algo wapo en el oho' y aprovecha para atacarlo en la cercanía.")
        var ataqueRoberto3=Ataque("Criptobro", 70,30, "intenta convencer al rival de invertir en una estafa piramidal de NFT.")
        var ataqueRoberto4=Ataque("Veganity", 20,80, "quiere bajar la moral al enemigo a base de criticar su participación en la masacre de la industria cárnica.")
        var ataquesRoberto=ArrayList<Ataque>()
        ataquesRoberto.add(ataqueRoberto1)
        ataquesRoberto.add(ataqueRoberto2)
        ataquesRoberto.add(ataqueRoberto3)
        ataquesRoberto.add(ataqueRoberto4)
        var roberto=Personaje("Robe", "cm_roberto.png", precioNormal, false, ataquesRoberto)

        var ataqueNorberto1=Ataque("Fardo",50,50, "arroja un fardo de cocaína al enemigo! Fresquito, recién descargado de la lancha.")
        var ataqueNorberto2=Ataque("SexSymbol", 80,20, "saca a relucir sus dotes de seducción para conquistar al enemigo. Le hace ojitos.")
        var ataqueNorberto3=Ataque("Luffy", 40,70, "da una chapa insoportable sobre la superioridad de One Piece frente al resto de animes. Puede convencer al rival.")
        var ataqueNorberto4=Ataque("Coleta", 60,40, "se quita la coleta, golpeando con la melena en slow-motion al enemigo.")
        var ataquesNortberto=ArrayList<Ataque>()
        ataquesNortberto.add(ataqueNorberto1)
        ataquesNortberto.add(ataqueNorberto2)
        ataquesNortberto.add(ataqueNorberto3)
        ataquesNortberto.add(ataqueNorberto4)
        var norberto=Personaje("Norberto", "cm_nortberto.png", precioNormal, false, ataquesNortberto)

        var ataqueEdu1=Ataque("SlamDunk", 50,50, "salta para hacerle un poster guapo al rival.")
        var ataqueEdu2=Ataque("Descanso", 60,40,",el cabrón, se queda dormido en clase para intentar descolocar al enemigo.")
        var ataqueEdu3=Ataque("Gorrilla", 80,20, "pide el mechero al rival con la clara intención de pincharselo. Como buén yonki.")
        var ataqueEdu4=Ataque("Kobe", 30,70, "se flipa con un discurso de Kobe Bryant y da la chapa al rival sobre la importancia del trabajo duro. Mientras, se lía un piti.")
        var ataquesEdu=ArrayList<Ataque>()
        ataquesEdu.add(ataqueEdu1)
        ataquesEdu.add(ataqueEdu2)
        ataquesEdu.add(ataqueEdu3)
        ataquesEdu.add(ataqueEdu4)
        var edu=Personaje("Edu", "cm_edu.png", precioNormal, false, ataquesEdu)

        var ataqueMiguel1=Ataque("Chicki chicken", 50,50, "invoca al 'Pollo del aprobado' para ensordecer al enemigo.")
        var ataqueMiguel2=Ataque("HackBag", 70,30, "trae una deconstrucción de ordenador en la mochila, con la que pretende hackear al rival.")
        var ataqueMiguel3=Ataque("Peña", 80,20, "da la chapa al enemigo para reclutarlo para la peña más antigua de Granada.")
        var ataqueMiguel4=Ataque("Youtuber",40,60, "crea un videotutorial sobre cómo partirle la cara al rival. Está lleno de efectos rarunos que marean al enemigo.")
        var ataquesMiguel=ArrayList<Ataque>()
        ataquesMiguel.add(ataqueMiguel1)
        ataquesMiguel.add(ataqueMiguel2)
        ataquesMiguel.add(ataqueMiguel3)
        ataquesMiguel.add(ataqueMiguel4)
        var miguel=Personaje("Lord Zermuzo", "cm_miguel.png", precioBoss, true, ataquesMiguel)

        var ataqueSalvador1=Ataque("Desmatricular", 100,10, "quiere desmatricular al rival por vías legales!")
        var ataqueSalvador2=Ataque("Aumento", 80,20, "aumenta el precio de la matrícula, dejando al rival en bancarrota.")
        var ataqueSalvador3=Ataque("Pitufillo", 50,50, "pide al rival a que le invite a un pituo y un café. Pretende insistir hasta dejarlo insconsciente!")
        var ataqueSalvador4=Ataque("MiniYo", 30,70, "ataca invocando a Salva Jr, una clon genéticamente idéntico, pero más joven y ágil.")
        var ataquesSalvador=ArrayList<Ataque>()
        ataquesSalvador.add(ataqueSalvador1)
        ataquesSalvador.add(ataqueSalvador2)
        ataquesSalvador.add(ataqueSalvador3)
        ataquesSalvador.add(ataqueSalvador4)
        var salvador=Personaje("Da Boss", "cm_salvador.png", precioBoss+10000, true, ataquesSalvador)



        DAOPersonaje.guardarPersonaje(alex)
        DAOPersonaje.guardarPersonaje(antonio)
        DAOPersonaje.guardarPersonaje(roberto)
        DAOPersonaje.guardarPersonaje(norberto)
        DAOPersonaje.guardarPersonaje(edu)
        DAOPersonaje.guardarPersonaje(miguel)
        DAOPersonaje.guardarPersonaje(salvador)

    }


    fun generaPersonajesAleatorios(){


        var array= arrayOf("Naruto", "Sasuke", "Kakashi", "Tsunade", "Jiraiya", "Sakura", "Rock Lee", "Madara")
        for (i in 0..array.size-1){

            var ataques:ArrayList<Ataque> =ArrayList<Ataque>()
            var nombre:String=array[i]
            var atq=nombre.substring(0,nombre.length-1)+"azo"

            for (i in 1..4){
                var ataque: Ataque = Ataque(atq+i, 30, 70,
                    "pega un "+atq);
                ataques.add(ataque)
            }

            var personaje: Personaje = Personaje(nombre, nombre+".png", 10000, false, ataques)
            DAOPersonaje.guardarPersonajePartida(personaje, "Konohagakure")

        }
    }


}