package e.pop.pongchallenge

import android.content.Context

import Object.*
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.*
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.score_activity.view.*
import kotlin.random.Random

class GameView:SurfaceView,SurfaceHolder.Callback{

    var joueur:Player?=null

    var txtView:TextView?=null

    //Boucle de Jeu
    var loop:GameLoop?=null
    var isRunning:Boolean?= null

    //Les nouvelles positions que doivent atteindre le joueur : Position où on a touché l'écran pour la dernière fois
    private var newPosX:Float?=null
    private var newPosY:Float?=null

    //Variable qui gère le score
    var score:Int?=null

    //Position d'origine en X
    private val ORIGIN_X = 10

    //Tableau d'Ennemis dans lequel on instancie nos ennemis
    var wave:Array<Ennemis>?=null

    //Largeur et hauteur de l'écran
    var wScreen:Int?= null
    var hScreen:Int?= null


    constructor(context:Context,loop: GameLoop) : super(context){
        holder.addCallback(this)
        this.loop=loop

        joueur = Player("Bob")
        joueur?.projectiles?.add(Projectiles(joueur?.posX as Float, joueur?.posY as Float))

        wave = Array(5,init = {i -> generateEnnemis(Random.nextFloat())})

        txtView = findViewById(R.id.score)

        isRunning=false
        score=0
    }

    constructor(context:Context,attrs:AttributeSet) : super(context,attrs){
        holder.addCallback(this)
        loop=GameLoop(this)

        joueur = Player("Bob")
        joueur?.projectiles?.add(Projectiles(joueur?.posX as Float, joueur?.posY as Float))

        wave = Array(5,init = {i -> generateEnnemis(Random.nextFloat())})

        score=0
        isRunning=false
        txtView = findViewById(R.id.score)
    }

    constructor(context:Context,attrs:AttributeSet,defStyleAttr:Int) : super(context,attrs,defStyleAttr){
        holder.addCallback(this)
        loop=GameLoop(this)

        joueur = Player("Bob")
        joueur?.projectiles?.add(Projectiles(joueur?.posX as Float, joueur?.posY as Float))

        isRunning=false
        wave = Array(5,init = {i -> generateEnnemis(Random.nextFloat())})
        txtView = findViewById(R.id.score)
        score=0
    }


    /**
     * Dessine l'écran de jeu
     */
    override fun draw(canvas: Canvas){
        super.draw(canvas)
        canvas.drawColor(Color.BLACK)

        joueur?.draw(canvas,joueur?.posX as Float,(hScreen as Int - 150f))

        for(i in joueur?.projectiles as ArrayList){
            i.draw(canvas)
        }


        for(i in 0 until (wave?.size as Int)) {
            if (wave?.get(i)?.isAlive as Boolean) {
                val posX = wave?.get(i)?.posX
                val posY = wave?.get(i)?.posY

                wave?.get(i)?.draw(canvas, posX as Float, posY as Float)
            }
        }

        txtView?.text = "Score : "+score
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
            val tailleZone:Float = (wScreen as Int)/5 - 0f


            /*
            Défini la position de l'ennemi dans la vague qui arrive
             */
            val posX:Float = (ORIGIN_X+tailleZone/3) + i*tailleZone
            /*
                Vérifie si un ennemi est toujours en vie et est sur l'écran
                Si c'est le cas on le fait bouger
                Sinon on en génére un nouveau
             */
            if( wave?.get(i)?.isAlive as Boolean && wave?.get(i)?.isOnScreen as Boolean) {
                wave?.get(i)?.moove()
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
            if((wave?.get(i)?.posY as Float) > hScreen as Int) wave?.get(i)?.isOnScreen = false
            if(wave?.get(i)?.pv as Int <= 0){
                wave?.get(i)?.isAlive = false
                score = score as Int + wave?.get(i)?.valueEnnemis as Int
            }
        }

        /*
        Déplacement des projectiles
         */
        if(!(joueur?.projectiles?.isEmpty() as Boolean)) {
            for (i in 0 until joueur?.projectiles?.size as Int) {
                val current = joueur?.projectiles?.get(i)
                current?.move()

                for(j in 0 until wave?.size as Int){
                    if(collisionProj( wave?.get(j) as Personnage,joueur?.projectiles?.get(i) as Projectiles)){
                        wave?.get(j)?.pv =  wave?.get(j)?.pv as Int - joueur?.attack as Int
                    }
                }
                if(!(current?.isActivate as Boolean)) joueur?.projectiles?.set(i,Projectiles(joueur?.posX as Float, joueur?.posY as Float))
            }

        }

        /*
        Ajout des projectiles
         */
        if((joueur?.projectiles?.size as Int) < 30)
            joueur?.projectiles?.add(Projectiles(joueur?.posX as Float, joueur?.posY as Float))

        /*
        Arrêt du jeu si le joueur meurt
         */
        if(joueur?.pv == 0) {
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
    fun generateEnnemis(posEnnemi:Float):Ennemis{
        val levelEnnemis = 1

        val newEnnemi=Ennemis(levelEnnemis,1)

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

    /**
     * Vérifie si l'objet x entre en collision avec l'objet y :=> Ici pour le joueur et les ennemis
     */
    private fun collision(x:Personnage?,y:Personnage?):Boolean {
        if(x !=null && y != null) {
            return(y.posX as Float >= x.posX as Float
                    && (y.posX as Float) < (x.posX as Float + (x.width ?: 50))
                    && (y.posY as Float) >= (x.posY as Float)
                    && (y.posY as Float) < (x.posY as Float + (x.height ?: 50)))
        }
        else return false
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

    /**
     * Methode de la SurfaceView
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

        newPosX = event?.getX()
        newPosY = event?.getY()

        return true
    }

}