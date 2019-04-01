package Object

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Projectiles {

    var posX:Float? = null
    var posY:Float? = null
    var vitesseY:Int?= null
    var objet:RectF?=null

    var width:Int?=null
    var height:Int?=null

    var isActivate:Boolean?=null

    constructor(x:Float,y:Float){
        posX=x
        posY=y
        vitesseY=35
        isActivate=true

        width = 15
        height = 15
    }

    fun move(){
        posY= (posY as Float)-(vitesseY as Int)
        if((posY as Float) < 0) isActivate=false
    }

    fun draw(c:Canvas){

        val paint= Paint()
        paint.color = Color.RED

        objet=RectF(posX as Float ,posY as Float,(posX as Float)+width as Int,(posY as Float)+height as Int)
        c.drawOval(objet as RectF,paint)

    }
}