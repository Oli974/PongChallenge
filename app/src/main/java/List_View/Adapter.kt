package List_View

import android.content.Context
import android.widget.ArrayAdapter
import android.view.*
import java.util.*
import Object.Score
import android.widget.TextView
import e.pop.pongchallenge.R


class Adapter:ArrayAdapter<Score>{

    var contxt:Context?=null

    constructor(context:Context,score : ArrayList<Score>) : super(context, R.layout.cell_layout,score){
        contxt=context
    }

    override fun getView(position:Int,convertView:View?,parent:ViewGroup):View
    {
        var cellView:View? = convertView

        if (cellView == null) {
            val inflater:LayoutInflater? = contxt?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            cellView = inflater?.inflate(R.layout.cell_layout, parent, false) as View
        }

        val nomPersonnage:TextView = cellView.findViewById(R.id.nom_joueur)
        val score:TextView = cellView.findViewById(R.id.score)
        val date:TextView = cellView.findViewById(R.id.date)
        val id:TextView = cellView.findViewById(R.id.id)


        val s:Score? = getItem(position)

        nomPersonnage.text = "Nom Joueur : "+s?.getNom()
        score.text = "Score : "+s?.getScore().toString()
        date.text = "Date : "+s?.getdate()

        return cellView
    }
}