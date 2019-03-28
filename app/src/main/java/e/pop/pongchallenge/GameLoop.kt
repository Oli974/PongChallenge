package e.pop.pongchallenge

import android.graphics.Canvas

class GameLoop : Thread {

    var view:GameView?=null


    /**
     * Gestion du frameRate
     */
    val IPS:Int=30
    val IPMS:Int= 1000/IPS // 1000ms/30

    /**
     * Gestion du Thread
     */
    private var running:Boolean=false //Etat du Thread

    constructor(view:GameView):super(){
        this.view=view
    }

    fun setRunning(boolean: Boolean){
        running=boolean
    }

    override fun run() {

        var startTime:Long ?= null
        var sleepTime:Long ?= null

        while(running){
            startTime= System.currentTimeMillis()

            synchronized(view?.holder as Any) { view?.update() } //Mise à jour des déplacements

            var c:Canvas?=null
            try {
                c = view?.holder?.lockCanvas()
                synchronized(view?.holder as Any) { view?.draw(c)}
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

}