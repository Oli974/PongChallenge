package e.pop.pongchallenge

import android.content.Context
import android.view.SurfaceHolder
import android.view.SurfaceView

import Object.*
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import kotlin.random.Random

class GameView:SurfaceView,SurfaceHolder.Callback {

    private var joueur:Player?=null
    private var loop:GameLoop?=null
    private var newPosX:Float?=null
    private var newPosY:Float?=null
    private var score:Int?=null
    private val ORIGIN_X = 10
    var wave:Array<Ennemis>?=null

    var wScreen:Int?= null
    var hScreen:Int?= null

    var ennemi:Ennemis?= null

    constructor(context:Context) : super(context){
        holder.addCallback(this)
        loop=GameLoop(this)

        joueur = Player("Bob")
        joueur?.projectiles?.add(Projectiles(joueur?.posX as Float, joueur?.posY as Float))

        wave = Array(5,init = {i -> generateEnnemis(Random.nextFloat())})

    }

    /**
     * Dessine l'écran de jeu
     */
    fun drawScreen(canvas: Canvas){
        canvas.drawColor(Color.BLACK)
        joueur?.draw(canvas,joueur?.posX as Float,(hScreen as Int - 150f))

        for(i in joueur?.projectiles as ArrayList){
            i.draw(canvas)
        }


        for(i in 0 until (wave?.size as Int)){
            var posX= wave?.get(i)?.posX
            var posY= wave?.get(i)?.posY

            wave?.get(i)?.draw(canvas,posX as Float,posY as Float)
        }
    }

    /**
     * Mise à jour du déplacement des objets (Appelés par la boucle Principale)
     */
    fun update(){
        val newX = newPosX ?: joueur?.posX
        val newY = newPosY ?: joueur?.posY

        joueur?.moove(newX as Float,newY as Float)

        /*
        On génère une position en x aléatoire où va apparaitre l'ennemi
         */

        for(i in 0 until (wave?.size as Int)){
            var randomPosX = (Random.nextFloat()*(wScreen as Int))
            val tailleZone:Float = (wScreen as Int)/5 - 0f

            val posX:Float = (ORIGIN_X+tailleZone/3) + i*tailleZone
            /*
                Vérifie si un ennemi est toujours en vie
                Si c'est le cas on le fait bouger
                Sinon on en génére un nouveau
             */
            if( wave?.get(i)?.isAlive as Boolean) wave?.get(i)?.moove()
            else{
                wave?.set(i,generateEnnemis(posX))
            }

            /*
            Si un ennemi quitte l'écran on le considère comme mort
             */
            if((wave?.get(i)?.posY as Float) > hScreen as Int){
                wave?.get(i)?.isAlive = false
            }
        }

        /*
        Déplacement et ajout de nouveaux projectiles
         */
        if(!(joueur?.projectiles?.isEmpty() as Boolean)) {
            for (i in 0 until joueur?.projectiles?.size as Int) {
                var current = joueur?.projectiles?.get(i)
                current?.move()
                if(!(current?.isActivate as Boolean)) joueur?.projectiles?.set(i,Projectiles(joueur?.posX as Float, joueur?.posY as Float))
            }

        }

        if((joueur?.projectiles?.size as Int) < 10)
                joueur?.projectiles?.add(Projectiles(joueur?.posX as Float, joueur?.posY as Float))

        /*
        Arrêt du jeu si le joueur meurt
         */
        if(joueur?.pv ==0) loop?.setRunning(false)
    }

    /**
     * Génération d'ennemis
     */
    fun generateEnnemis(posEnnemi:Float):Ennemis{
        var levelEnnemis = 1

        var newEnnemi=Ennemis(levelEnnemis)

        newEnnemi.posX=posEnnemi

        /*
        La vitesse des ennemis évolue en fonction du niveau de ceux-ci
         */
        when(levelEnnemis){
            in 1..3 -> newEnnemi.vitesseY = 10
            in 4..5 -> newEnnemi.vitesseY = 15
            in 5..7 -> newEnnemi.vitesseY = 17
            in 7..10 -> newEnnemi.vitesseY =20
        }

        return newEnnemi
    }


    override fun surfaceCreated(holder: SurfaceHolder?) {
        if(loop?.state == Thread.State.TERMINATED) {
            loop = GameLoop(this)
        }

        loop?.setRunning(true)
        loop?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        hScreen=height
        wScreen=width

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