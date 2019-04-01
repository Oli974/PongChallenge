package Object

import java.util.*

class Score{

    private var Id:Long?=null
    private var nomJoueur:String?=null
    private var score:Int?=null
    private var date:String?=null

    constructor(id:Long,nom:String,score:Int,date: String){
        this.Id = id
        nomJoueur=nom
        this.score=score
        this.date=date
    }

    fun setNom(nom:String) {nomJoueur=nom}
    fun setScore(sc:Int) {score= sc}
    fun setDate(dt:String) {date=dt}

    fun getID():Long{return Id as Long}
    fun getNom():String{return nomJoueur as String}
    fun getScore():Int{return score as Int}
    fun getdate():String{return date as String}


}