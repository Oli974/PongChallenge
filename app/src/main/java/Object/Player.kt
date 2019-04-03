package Object

import android.graphics.*
import android.os.Handler

class Player:Personnage {

    var name:String?=null
    var rectf:RectF?=null
    var projectiles:ArrayList<Projectiles>?= null
    var isAttack:Boolean?=null

    var handler: Handler = Handler()
    var runnable = Thread()



    constructor(name:String):super(){
        this.name=name
        projectiles = ArrayList()
        isAttack = false
    }

    /**
     * Méthodes
     */

    /*
    Défini la manière d'attaquer du joueur
     */
    override fun attack() {

        runnable = object : Thread() {

            override fun run() {
                while(isAttack as Boolean) {
                    if ((projectiles?.size as Int) < 50) {
                        addQueue()
                    } else {
                        synchronized(this) {
                            for (i in 0 until projectiles?.size as Int) {
                                if (!(projectiles?.get(i)?.isActivate() as Boolean) || projectiles?.get(i)?.hasHit() as Boolean) {
                                    projectiles?.set(i, generateProjectile())
                                    sleep(50)
                                }
                            }
                        }
                    }
                }
            }

            fun addQueue(){
                synchronized(this) {
                    projectiles?.add(Projectiles(posX as Float, posY as Float,false))
                }
            }

        }

        runnable.start()
    }


    fun generateProjectile():Projectiles{
        return Projectiles(posX as Float, posY as Float,false)
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