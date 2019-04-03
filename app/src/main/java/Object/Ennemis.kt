package Object

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.preference.PreferenceScreen
import kotlin.random.Random

class Ennemis:Personnage{

    var rectf:RectF?=null

    var isOnScreen:Boolean?=null

    var typeEnnemi:Int?=null
    var valueEnnemis:Int?=null

    var inPosition:Boolean?=null
    var inAttack:Boolean?=null
    var goBack:Boolean?=null

    var projectiles:ArrayList<Projectiles>?=null
    var runnable = Thread()

    constructor(level:Int,type:Int):super(level){
        isOnScreen=true
        typeEnnemi=type

        if(typeEnnemi as Int> 0)  valueEnnemis=50 * type
        else valueEnnemis = 50

        inAttack=false
        goBack=false
        inPosition=false

        projectiles = ArrayList()

    }

    override fun attack() {
        if(posY as Float >= (hEcran as Int)/2){
            when(typeEnnemi){
                1 -> {
                    val LaunchAttack = Random.nextFloat()

                    if(LaunchAttack < 0.1 && (inPosition as Boolean)){
                        vitesseY = 50
                        inAttack = true
                    }

                }
            }
        }
    }

    override fun moove() {
        when(typeEnnemi){
            0 -> {
                posY = (posY as Float) + (vitesseY as Int)
                posX = posX as Float + vitesseX as Int

                    if((posX as Float >= wEcran as Int) || (posX as Float <= 0)){
                        vitesseX = -(vitesseX as Int)
                    }

            }
            1 -> {

                /*
                Si l'ennemi est sur la partie supérieure de l'écran
                Il avance jusqu'à la moitié de l'écran
                 */
                if((posY as Float) < (hEcran as Int)/2){
                    posY = (posY as Float) +(vitesseY as Int)
                }

                /*
                Si il est arrivé à la moitié de l'écran et ne doit pas attaquer
                il bouge à l'horizontal
                 */
                if(posY as Float >= (hEcran as Int)/2 && !(inAttack as Boolean) && !(goBack as Boolean)){
                    posX = posX as Float + vitesseX as Int
                    inPosition=true
                    if((posX as Float >= wEcran as Int) || (posX as Float <= 0)){
                        vitesseX = -(vitesseX as Int)
                    }
                }

                /*
                Si il a attaqué et qu'il doit retourner à sa position d'origine
                 */
                else if(posY as Float >= (hEcran as Int/2) && goBack as Boolean){
                    posY = posY as Float + vitesseY as Int

                    /*
                    Quand il arrive à sa position d'origine on marque goBack à false
                    et on remet à la vitesse une valeur positive
                     */
                    if((posY as Float <= (hEcran as Int)/2)){
                        goBack = false
                        vitesseY = -(vitesseY as Int)
                    }
                }
                else if(inAttack as Boolean){
                    posY = posY as Float + vitesseY as Int
                    inPosition=false
                    /*
                    Quand il arrive dans le dernier dixième de l'écran
                    Il arrête son attaque et repart vers sa position d'origine
                     */
                    if((posY as Float >= 0.9*hEcran as Int)){
                        goBack=true
                        inAttack=false
                        vitesseY = -(vitesseY as Int)
                    }
                }
            }
        }
    }

    override fun moove(x: Float, y: Float) {

        if((posX as Float) < x ) {
            posX= posX as Float + vitesseX as Int
        }else if((posX as Float >= x)) posX = posX as Float - vitesseX as Int

        posY = posY as Float + vitesseY as Int

    }



    fun draw(canvas: Canvas){

        val paint= Paint()
        paint.color = Color.WHITE


        rectf= RectF(posX as Float,posY as Float,posX as Float+(width ?: 50),posY as Float+(height?:50))
        canvas.drawRect(rectf as RectF,paint)
    }
}