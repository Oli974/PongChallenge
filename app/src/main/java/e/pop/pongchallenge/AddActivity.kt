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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_score)

        score = intent.getIntExtra("score",0)
        nomEditText=findViewById(R.id.nouveauJoueur)
    }

    fun OnClickEvent(v: View){

        when(v.id){
            R.id.ok -> {
                val intent:Intent = Intent(this,listActivity::class.java)
                intent.putExtra("nomJoueur",nomEditText?.text)
                intent.putExtra("score",score)
                intent.putExtra("isScore",true)
                try{
                    startActivity(intent)
                }catch (e:ActivityNotFoundException){}
            }
            R.id.Cancel -> {
                val intent:Intent = Intent(this,listActivity::class.java)
                intent.putExtra("nomJoueur","Guest")
                intent.putExtra("score",score)
                intent.putExtra("isScore",true)
                try{
                    startActivity(intent)
                }catch (e:ActivityNotFoundException){}
            }


        }

    }
}