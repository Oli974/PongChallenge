package e.pop.pongchallenge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {

    var nouvellePartie:Button?=null
    var tableauDesScores:Button?=null
    var nomJoueur:EditText?=null

    val NOUVELLE_PARTIE = 0
    val TABLEAU_SCORE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nouvellePartie = findViewById(R.id.new_game)
        tableauDesScores = findViewById(R.id.tableau_scores)
        nomJoueur = findViewById(R.id.nom_joueur)
    }

    fun onClickEvent(v: View){
        when(v){
            nouvellePartie -> {
                val intent = Intent(this, GameActivity::class.java)
                startActivityForResult(intent,NOUVELLE_PARTIE)
            }

            tableauDesScores -> {
                val intent = Intent(this,listActivity::class.java)
                startActivityForResult(intent,TABLEAU_SCORE)
            }

        }
    }
}
