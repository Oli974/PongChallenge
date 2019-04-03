package Object

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.BoringLayout

class Projectiles {

    var posX:Float? = null
    var posY:Float? = null
    var vitesseY:Int?= null
    var objet:RectF?=null

    var width:Int?=null
    var height:Int?=null

    private var isActivate:Boolean?=null
    private var hasHit:Boolean?=null
    private var isAIball:Boolean?=null

    constructor(x:Float,y:Float,bool: Boolean){
        posX=x
        posY=y
        vitesseY=35
        isActivate=true
        hasHit=false

        isAIball=bool

        width = 15
        height = 15
    }

    fun move(){
        if(isActivate as Boolean && !hasHit()){
            posY= (posY as Float)-(vitesseY as Int)
        }

        if((posY as Float) < 0 || hasHit as Boolean) {
            isActivate=false
        }
    }

    fun setHasHit(bool:Boolean){
        hasHit = bool
    }
    fun hasHit():Boolean{return hasHit as Boolean}

    fun isActivate():Boolean{
        return isActivate as Boolean
    }

    fun draw(c:Canvas){

        val paint= Paint()
        if(isAIball as Boolean) paint.color = Color.RED
        else paint.color = Color.GREEN

        objet=RectF(posX as Float ,posY as Float,(posX as Float)+width as Int,(posY as Float)+height as Int)
        if(isActivate() && !hasHit()) c.drawOval(objet as RectF,paint)

    }
}