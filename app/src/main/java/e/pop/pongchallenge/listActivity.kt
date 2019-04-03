package e.pop.pongchallenge

import List_View.*
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import Object.*
import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class listActivity : AppCompatActivity() {

    private var listView:ListView?=null
    private var adapter: Adapter?=null
    private var db_adapter:dbAdapter?=null

    private val add = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.score_activity)

        db_adapter= dbAdapter(this)
        db_adapter?.open()

        if(intent.getBooleanExtra("Add",false)){
            val score = intent.getIntExtra("score",0)

            val intent = Intent(this,AddActivity::class.java)
            intent.putExtra("Score",score)

            startActivityForResult(intent,add)
        }

        listView= findViewById(R.id.list)
        registerForContextMenu(listView)

        val scoresTab = db_adapter?.getAllScore() as ArrayList<Score>
        adapter= Adapter(this,scoresTab)
        listView?.adapter=adapter
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu,menu)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == add){
            when(resultCode){
                Activity.RESULT_OK -> {
                    val score = intent.getIntExtra("score",0)
                    var nomjoueur = data?.getStringExtra("nomJoueur")

                    if(nomjoueur?.isBlank() as Boolean){
                        nomjoueur="guest"
                    }

                    val formatter = SimpleDateFormat.getDateInstance()
                    val dt = Date()
                    val dtStr = formatter.format(dt)

                    val id:Long = db_adapter?.insertScore(nomjoueur,dtStr,score) as Long

                    println("Insertion du score avec l'id : $id nom : $nomjoueur score : $score date : $dtStr")

                    adapter?.add(db_adapter?.getScore(id))
                    finish()
                }

                Activity.RESULT_CANCELED -> {
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        db_adapter?.close()
        super.onDestroy()
    }
}