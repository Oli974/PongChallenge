package Object

import android.graphics.*

class Player:Personnage {

    var name:String?=null
    var rectf:RectF?=null

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

    override fun moove(x:Float,y:Float) {

        val dX:Float?

        if((posX as Float) < x){
            dX = x - posX as Float
            //Si la vitesse est plus grande que la distance restante on réduit la vitesse
            if((dX-vitesseX as Int) < 0f) vitesseX=(vitesseX as Int) - 1

            posX= posX as Float + vitesseX as Int
        }
        else if((posX as Float) > x){
            dX = posX as Float - x
            if((dX-vitesseX as Int) < 0f) vitesseX=(vitesseX as Int) - 1
            else vitesseX = 10

            posX= posX as Float - vitesseX as Int
        }

    }

    fun draw(canvas:Canvas,x:Float,y:Float){
        posX=x
        posY=y

        val paint=Paint()
        paint.color = Color.WHITE


        rectf=RectF(x,y,x+50,y+50)
        canvas.drawRect(rectf as RectF,paint)
    }
}