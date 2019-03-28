package Object

abstract class Personnage {

    /**
      *Attributs de personnages
      */

        /*
         Attributs de base
         */
        var pv:Int?= null
        var attack:Int?= null
        var level:Int?= null

        /*
         Position selon les axes x et y
         */
        var posX:Int?=null
        var posY:Int?=null

        /*
        Vitesse de déplacement selon les coordonnées x et y
         */
        var vitesseX:Int?= null
        var vitesseY:Int?= null

    /**
     * Attributs globaux
     */

    var wEcran:Int?=null
    var hEcran:Int?=null

    /**
      *Constructeur employé lors de la première création du personnage
      */
    constructor(){
        pv = 100
        attack = 15
        level = 1

        vitesseX=10
        vitesseY=10
    }

    constructor(level: Int){
        pv = 100 + level*25
        attack = 15 + level*5
        this.level= level

        vitesseX=10
        vitesseY=10
    }

    /**
     * Méthodes du personnage
     */

    fun resize(wScreen: Int, hScreen: Int) {
        // wScreen et hScreen sont la largeur et la hauteur de l'écran en pixel
        // on sauve ces informations en variable globale, car elles serviront
        // à détecter les collisions sur les bords de l'écran
        wEcran = wScreen
        hEcran = hScreen

        // on définit (au choix) la taille de la balle à 1/5ème de la largeur de l'écran
        var width:Int = wScreen / 5
        var height:Int = wScreen / 5
    }

    abstract fun moove()
    abstract fun moove(x:Int,y:Int)
    abstract fun attack()
}