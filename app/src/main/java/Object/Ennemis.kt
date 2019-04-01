package Object

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Ennemis:Personnage{

    var rectf:RectF?=null
    var isOnScreen:Boolean?=null
    var typeEnnemi:Int?=null
    var valueEnnemis:Int?=null

    constructor(level:Int,type:Int):super(level){
        isOnScreen=true
        typeEnnemi=type
        valueEnnemis=50*type
    }

    override fun attack() {
    }

    override fun moove() {
        posY = (posY as Float) + (vitesseY as Int)
    }

    override fun moove(x: Float, y: Float) {
    }

    fun draw(canvas: Canvas, x:Float, y:Float){
        posX=x
        posY=y

        val paint= Paint()
        paint.color = Color.WHITE


        rectf= RectF(x,y,x+(width ?: 50),y+(height?:50))
        canvas.drawRect(rectf as RectF,paint)
    }
}