package Object

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Ennemis(level:Int):Personnage(level){

    var rectf:RectF?=null

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


        rectf= RectF(x,y,x+50,y+50)
        canvas.drawRect(rectf as RectF,paint)
    }
}