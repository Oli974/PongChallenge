package e.pop.pongchallenge

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText

class AddActivity :AppCompatActivity(){

    var nomEditText:EditText?=null
    var score:Int?=null

    val NOMJOUEUR = "nomJoueur"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_score)

        score = intent.getIntExtra("Score",0)
        nomEditText=findViewById(R.id.nouveauJoueur)
    }

    fun OnClickEvent(v: View){

        when(v.id){
            R.id.ok -> {
                val intent = Intent(this,listActivity::class.java)
                intent.putExtra(NOMJOUEUR,nomEditText?.text)
                intent.putExtra("score",score)
                intent.putExtra("isScore",true)
                try{
                    setResult(Activity.RESULT_OK,intent)
                    finish()
                }catch (e:ActivityNotFoundException){}
            }
        }

    }
}