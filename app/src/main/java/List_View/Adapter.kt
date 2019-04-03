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

        val nomView:TextView = cellView.findViewById(R.id.nom_joueur)
        val scoreView:TextView = cellView.findViewById(R.id.score)
        val dateView:TextView = cellView.findViewById(R.id.date)

        val s:Score? = getItem(position)

        val nom = "Nom Joueur : ${s?.getNom()}"
        val score = "Score : "+s?.getScore().toString()
        val date = "Date : "+s?.getdate()

        nomView.text = nom
        scoreView.text = score
        dateView.text = date

        return cellView
    }
}