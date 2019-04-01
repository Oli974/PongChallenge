package Object

import android.graphics.*

class Player:Personnage {

    var name:String?=null
    var rectf:RectF?=null
    var projectiles:ArrayList<Projectiles>?= null

    constructor(name:String):super(){
        this.name=name
        projectiles = ArrayList()
    }

    /**
     * Méthodes
     */

    /*
    Défini la manière d'attaquer du joueur
     */
    override fun attack() {
    }


    /* Methode de mouvement du joueur */

    override fun moove() {
    }

    override fun moove(x:Float,y:Float) {
        posX = x
    }

    fun draw(canvas:Canvas,x:Float,y:Float){
        posX=x
        posY=y

        val paint=Paint()
        paint.color = Color.WHITE

        rectf=RectF(x,y,x+(width as Int),y+(height as Int))
        canvas.drawRect(rectf as RectF,paint)
    }
}