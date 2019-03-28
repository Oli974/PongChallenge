package e.pop.pongchallenge

import android.content.Context
import android.view.SurfaceHolder
import android.view.SurfaceView

import Object.*
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent

class GameView:SurfaceView,SurfaceHolder.Callback {

    var joueur:Player?=null
    var loop:GameLoop?=null
    var newPosX:Int?=null
    var newPosY:Int?=null

    constructor(context:Context) : super(context){
        holder.addCallback(this)
        loop=GameLoop(this)

        joueur = Player("Bob")
    }

    /**
     * Dessine l'écran de jeu
     */
    fun drawScreen(canvas: Canvas) : Unit{
        if(canvas == null) return

        canvas.drawColor(Color.BLACK)

        joueur?.draw(canvas,20,20)
    }

    /**
     * Mise à jour du déplacement des objets (Appelés par la boucle Principale)
     */
    fun update(){
        joueur?.moove(newPosX ?: joueur?.posX as Int,newPosY ?: joueur?.posY as Int)
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if(loop?.state == Thread.State.TERMINATED){
            loop = GameLoop(this)
        }

        loop?.setRunning(true)
        loop?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        joueur?.resize(width,height)
    }


    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        var retry:Boolean = true
        while(retry){
            try{
                loop?.join()
                retry=false
            }catch (e:InterruptedException){}
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        newPosX = event?.getX() as Int?
        newPosY = event?.getY() as Int?
        return true
    }


}