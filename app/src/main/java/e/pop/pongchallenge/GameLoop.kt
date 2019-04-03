package e.pop.pongchallenge

import android.graphics.Canvas

class GameLoop : Thread {

    var view:GameView?=null
    var pause:Boolean?=null

    /**
     * Gestion du frameRate
     */
    private val IPS:Int=30
    private val IPMS:Int= 1000/IPS // 1000ms/30

    /**
     * Gestion du Thread
     */
    private var running:Boolean=false //Etat du Thread

    constructor(view:GameView):super(){
        this.view=view
        pause=false
    }

    fun setRunning(boolean: Boolean){
        running=boolean
    }

    override fun run() {

        var startTime:Long
        var sleepTime:Long

        while(running){
            startTime= System.currentTimeMillis()

            if(pause as Boolean){
                while(pause as Boolean){
                    Thread.sleep(200)
                }
            }

            synchronized(view?.holder as Any) { view?.update() } //Mise à jour des déplacements
            if(pause as Boolean){
                synchronized(this){

                }
            }
            var c:Canvas?=null
            try {
                c = view?.holder?.lockCanvas()
                synchronized(view?.holder as Any) { view?.draw(c as Canvas)}
            }finally {
                if(c!=null) view?.holder?.unlockCanvasAndPost(c)
            }

            sleepTime = IPMS - (System.currentTimeMillis() - startTime)
            try {
                if(sleepTime >=0) sleep(sleepTime)
            }catch (e:Exception){

            }
        }
    }

    fun setPause(){
        pause = !(pause as Boolean)
    }


    fun isRunning():Boolean{
        return running
    }
}