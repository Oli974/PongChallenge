package Object

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.shapes.OvalShape

class Projectiles {

    var posX:Float? = null
    var posY:Float? = null
    var vitesseY:Int?= null
    var objet:RectF?=null

    var isActivate:Boolean?=null

    constructor(x:Float,y:Float){
        posX=x
        posY=y
        vitesseY=35
        isActivate=true
    }

    fun move(){
        posY= (posY as Float)-(vitesseY as Int)
        if((posY as Float) < 0) isActivate=false
        print("Position projectile : ("+posX+","+posY+")")
    }

    fun draw(c:Canvas){

        val paint= Paint()
        paint.color = Color.RED

        objet=RectF(posX as Float ,posY as Float,(posX as Float)+15,(posY as Float)+15)
        c.drawOval(objet as RectF,paint)

    }
}