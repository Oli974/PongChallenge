package e.pop.pongchallenge

import android.content.Context

import Object.*
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.*
import android.widget.TextView
import kotlin.random.Random

class GameView:SurfaceView,SurfaceHolder.Callback{

    var joueur:Player?=null

    var scoreView:TextView?=null

    //Boucle de Jeu
    var loop:GameLoop?=null
    var isRunning:Boolean?= null

    //Les nouvelles positions que doivent atteindre le joueur : Position où on a touché l'écran pour la dernière fois
    private var newPosX:Float?=null
    private var newPosY:Float?=null

    //Variable qui gère le score
    var score:Int?=null

    //Position d'origine en X
    private val ORIGINX = 10

    //Tableau d'Ennemis dans lequel on instancie nos ennemis
    private var wave:Array<Ennemis>?=null

    //Largeur et hauteur de l'écran

    private var wScreen:Int?= null
    private var hScreen:Int?= null

    /**
     * Constructeurs de la vue de Jeu
     */

    constructor(context:Context,loop: GameLoop) : super(context){
        holder.addCallback(this)
        this.loop=loop

        joueur = Player("Bob")

        wave = Array(5,init = {generateEnnemis(Random.nextFloat())})

        scoreView = findViewById(R.id.score)

        isRunning=false
        score=0
    }

    constructor(context:Context,attrs:AttributeSet) : super(context,attrs){
        holder.addCallback(this)
        loop=GameLoop(this)

        joueur = Player("Bob")

        score=0

        wave = Array(5,init = {generateEnnemis(Random.nextFloat())})

        isRunning=false

        scoreView = findViewById(R.id.score)
    }

    constructor(context:Context,attrs:AttributeSet,defStyleAttr:Int) : super(context,attrs,defStyleAttr){
        holder.addCallback(this)
        loop=GameLoop(this)

        joueur = Player("Bob")

        isRunning=false
        score=0
        wave = Array(5,init = {generateEnnemis(Random.nextFloat())})
        scoreView = findViewById(R.id.score)
    }
    /**
     * Dessine l'écran de jeu
     * Est mis à jour dans la boucle de jeu dans le Thread gameLoop
     */
    override fun draw(canvas: Canvas){
        super.draw(canvas)

        joueur?.projectiles?.forEach { i -> i.draw(canvas) }

        joueur?.draw(canvas,joueur?.posX as Float,(0.9f * hScreen as Int))

        wave?.forEach { i ->
            i.resize(wScreen as Int ,hScreen as Int )
            i.draw(canvas)
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
        On génère la position en x où va apparaitre l'ennemi
         */
        for(i in 0 until (wave?.size as Int)){
            val tailleZone:Float = (wScreen as Int)/5 - 0f
            /*
            Défini la position de l'ennemi dans la vague qui arrive
            Les ennemis arrivant en vague ils sont disposés sur la même ligne
            avec un écart de wScreen/5 (avec wScreen la largeur de l'écran)
             */
            val posX:Float = (ORIGINX+tailleZone/3) + i*tailleZone
            /*
                Vérifie si un ennemi est toujours en vie et est sur l'écran
                Si c'est le cas on le fait bouger
                Sinon on en génére un nouveau
             */
            if( wave?.get(i)?.isAlive as Boolean && wave?.get(i)?.isOnScreen as Boolean) {

                if(wave?.get(i)?.typeEnnemi == 2) wave?.get(i)?.moove(joueur?.posX as Float, joueur?.posY as Float)
                else wave?.get(i)?.moove()


                wave?.get(i)?.attack()
                if(collision(wave?.get(i) as Ennemis, joueur as Player)){
                    joueur?.pv = joueur?.pv as Int - 25
                }
            }
            else{
                wave?.set(i,generateEnnemis(posX))
            }

            /*
            Si un ennemi quitte l'écran on le considère comme sorti
            Si ses pv sont inférieures à zéro on le considère comme mort et on augmente le score
             */
            if((wave?.get(i)?.posY as Float) > hScreen as Int && wave?.get(i)?.isAlive as Boolean) {
                wave?.get(i)?.isOnScreen = false
            }
            if(wave?.get(i)?.pv as Int <= 0){
                wave?.get(i)?.isAlive = false
                score = score as Int + wave?.get(i)?.valueEnnemis as Int
            }
        }


        /**
         * Gestion des projectiles du joueur
         * -> Déplacement
         * -> Collision avec un ennemi
         */

        /*
        Déplacement des projectiles (joueur)
         */
        if(!(joueur?.projectiles?.isEmpty() as Boolean)) {

           joueur?.projectiles?.forEach { i ->

                i.move()

                for(j in 0 until wave?.size as Int){
                    /*
                    Si il y a collision entre un projectile et un ennemi et que ce projectile est actif
                    On réduit la vie de l'ennemi du nombre de points d'attaque du joueur
                    On désactive le projectile
                    */
                    if(collisionProj( wave?.get(j) as Personnage,i) && !i.hasHit()){
                        wave?.get(j)?.pv =  wave?.get(j)?.pv as Int - joueur?.attack as Int
                        i.setHasHit(true)
                    }
                }
            }

        }

        /*
        Arrêt du jeu si le joueur meurt
         */
        if(joueur?.pv as Int <= 0) {
            isRunning=false
            loop?.setRunning(false)

            val intent = Intent(context,listActivity::class.java)
            intent.putExtra("Add",true)
            intent.putExtra("score",score)
            context?.startActivity(intent)
        }
    }

    /**
     * Génération d'ennemis
     */
    private fun generateEnnemis(posEnnemi:Float):Ennemis{
        val levelEnnemis = 1
        var typeEnnemis:Int?= null
        when(score){
            in 0..1000 ->     typeEnnemis = 0

            in 1001..2000 -> typeEnnemis = Random.nextInt(2)

            else ->       typeEnnemis = Random.nextInt(3)
        }
        val newEnnemi=Ennemis(levelEnnemis,typeEnnemis)
        newEnnemi.posX=posEnnemi
        /*
        La vitesse des ennemis évolue en fonction du niveau de ceux-ci
         */
        when(levelEnnemis){
            in 1..3 -> newEnnemi.vitesseY = 10
            in 4..5 -> newEnnemi.vitesseY = 15
            in 6..8 -> newEnnemi.vitesseY = 17
            else -> newEnnemi.vitesseY = 20
        }

        return newEnnemi
    }

    /**
     * Vérifie si l'objet x entre en collision avec l'objet y :=> Ici pour le joueur et les ennemis
     */
    private fun collision(x:Personnage?,y:Personnage?):Boolean {
            /*
            Algorithme de Collision d'un point dans un carré
            return(
                    (y.posX as Float >= x.posX as Float                                     //L'origine x de y >= l'origine x de x
                            && (y.posX as Float) < (x.posX as Float + (x.width ?: 50)))     //L'origine x de y < l'origine x + largeur de x

                    && ((y.posY as Float) >= (x.posY as Float) && (y.posY as Float) < (x.posY as Float + (x.height ?: 50))))
            */

            /*
            Algorithme de Collision pour deux objets carrés (HitBox)
             */
            val Xx = x?.posX as Float
            val Xy = x.posY as Float

            val Yx = y?.posX as Float
            val Yy = y.posY as Float

            val yWidth = y.width    as Int

            val xWidth = x.width as Int
            val xHeight = x.height  as Int

            return (
                      (
                        (Yx < (Xx + xWidth) && Yx > Xx) ||
                                ((Yx + yWidth > Xx)   && (Yx < Xx))
                      )
                            &&
                      (
                        (Yy < (Xy + xHeight) && Yy > Xy)
                      )
                    )
    }

    /**
     * Vérifie si un projectile entre en collision avec le personnage
     */
    private fun collisionProj(x:Personnage?,y:Projectiles?):Boolean {
        if(x !=null && y != null) {
            return(y.posX as Float >= x.posX as Float
                    && (y.posX as Float) < (x.posX as Float + (x.width ?: 50))
                    && (y.posY as Float) >= (x.posY as Float)
                    && (y.posY as Float) < (x.posY as Float + (x.height ?: 50)))
        }
        else return false
    }

    fun getScore():Int{return score as Int}

    /**
     * Méthode surchargés de la classe SurfaceView
     */

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if(loop?.state == Thread.State.TERMINATED) {
            loop = GameLoop(this)
        }

        loop?.setRunning(true)
        isRunning=true
        loop?.start()


    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        hScreen=height
        wScreen=width

        joueur?.resize(width,height)

        for(i in 0 until wave?.size as Int){
            wave?.get(i)?.resize(width,height)
        }
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
        newPosX = event?.x
        newPosY = event?.y

        if(!(joueur?.isAttack as Boolean)) {
            joueur?.isAttack = true
            joueur?.attack()
        }

        return true
    }

}