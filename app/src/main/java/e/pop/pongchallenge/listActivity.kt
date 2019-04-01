package e.pop.pongchallenge

import List_View.*
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import Object.*
import android.os.Bundle
import android.content.Intent
import android.view.MenuItem
import android.widget.AdapterView
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class listActivity : AppCompatActivity() {

    private var listView:ListView?=null
    private var adapter: Adapter?=null
    private var db_adapter:dbAdapter?=null

    var ArrayTest:ArrayList<Score> = arrayListOf(Score(0,"jean",12,"bob"))

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.score_activity)

        db_adapter= dbAdapter(this)
        db_adapter?.open()

        if(intent.getBooleanExtra("isTrue",false)){
            val score = intent.getIntExtra("score",0)
            val nomJoueur = intent.getStringExtra("nomJoueur")

            val dt = LocalDate.now()
            val dtFormatter = DateTimeFormatter.ofPattern("dd MM yyyy")

            val date:String = dt.format(dtFormatter)

            db_adapter?.insertScore(nomJoueur,date,score)
        }

        listView= findViewById(R.id.list)
        registerForContextMenu(listView)

        adapter= Adapter(this,db_adapter?.getAllScore() as ArrayList<Score>)
        listView?.adapter=adapter
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {

        val menuInfo:AdapterView.AdapterContextMenuInfo = item?.menuInfo as AdapterView.AdapterContextMenuInfo
        val s:Score? = adapter?.getItem(menuInfo.position)

        when(item.itemId){
            R.id.remove -> {
                adapter?.remove(s)
                db_adapter?.removeScore(s?.getID() as Long)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onDestroy() {
        db_adapter?.close()
        super.onDestroy()
    }
}