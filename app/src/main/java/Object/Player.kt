package Object

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Player:Personnage {

    var name:String?=null

    constructor(name:String):super(){
        this.name=name
    }






    /**
     * Méthodes
     */

    /*
    Défini la manière d'attaquer du joueur
     */
    override fun attack() {
    }

    override fun moove() {
    }

    override fun moove(x:Int,y:Int) {

        if((posX as Int) < x) posX= posX as Int + vitesseX as Int
        else posX= posX as Int - vitesseX as Int

        if((posY as Int) < y) posY= posY as Int + vitesseY as Int
        else posY= posY as Int - vitesseY as Int

    }

    fun draw(canvas:Canvas,x:Int,y:Int){
        posX=x
        posY=y

        var paint:Paint=Paint()
        paint.color = Color.WHITE

        var rect:Rect=Rect(x,y,x+50,y+50)
        canvas.drawRect(rect,paint)
    }
}