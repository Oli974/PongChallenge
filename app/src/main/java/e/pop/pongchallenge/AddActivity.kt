package e.pop.pongchallenge

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.TextView

class AddActivity :AppCompatActivity(){

    var nomEditText:EditText?=null
    var scoreView : TextView?= null
    var score:Int?=null
    var nomJoueur:String?=null

    val NOMJOUEUR = "nomJoueur"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_score)

        scoreView= findViewById(R.id.scoreTotal)

        scoreView?.text = "Score Final : ${intent.getIntExtra("Score",0)}"

        nomEditText=findViewById(R.id.nouveauJoueur)

    }

    fun OnClickEvent(v: View){
        when(v.id){
            R.id.ok -> {
                val intent = Intent(this,listActivity::class.java)

                nomJoueur = nomEditText?.text?.toString()

                intent.putExtra(NOMJOUEUR,nomJoueur)

                try{
                    setResult(Activity.RESULT_OK,intent)
                    finish()
                }catch (e:ActivityNotFoundException){}
            }
            else -> {

                try{
                    setResult(Activity.RESULT_CANCELED)
                }catch (e:ActivityNotFoundException){}
            }
        }
    }
}