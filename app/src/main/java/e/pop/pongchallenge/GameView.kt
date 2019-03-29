package e.pop.pongchallenge

import android.content.Context
import android.view.SurfaceHolder
import android.view.SurfaceView

import Object.*
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent

class GameView:SurfaceView,SurfaceHolder.Callback {

    private var joueur:Player?=null
    private var loop:GameLoop?=null
    private var newPosX:Float?=null
    private var newPosY:Float?=null
   // private var Score:Int?=null

    constructor(context:Context) : super(context){
        holder.addCallback(this)
        loop=GameLoop(this)

        joueur = Player("Bob")
    }

    /**
     * Dessine l'écran de jeu
     */
    fun drawScreen(canvas: Canvas){
        canvas.drawColor(Color.BLACK)

        joueur?.draw(canvas,joueur?.posX as Float,joueur?.posY as Float)
    }

    /**
     * Mise à jour du déplacement des objets (Appelés par la boucle Principale)
     */
    fun update(){

        val newX = newPosX ?: joueur?.posX
        val newY = newPosY ?: joueur?.posY

        joueur?.moove(newX as Float,newY as Float)
    }

    /**
     * Génération d'ennemis
     */
    /*
    fun randomEnnemiesGenerate(){
        var wave:Array<Ennemis>?=null



    }
    */

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
        var retry= true
        while(retry){
            try{
                loop?.join()
                retry=false
            }catch (e:InterruptedException){}
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        newPosX = event?.getX()
        newPosY = event?.getY()

        return true
    }


}