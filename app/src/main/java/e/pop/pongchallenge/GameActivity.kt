package e.pop.pongchallenge

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*

class GameActivity:AppCompatActivity(){

    private var btPause:Button?= null
    private var scoreView:TextView?=null

    private var gameView:GameView?=null
    private var gameLoop:GameLoop?=null

    private var score:Int?=null

    private var nomJoueur:String?=null

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)

        //set FullScreen
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //Set no Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.game_activity)

        gameView = findViewById(R.id.gameView)
        gameLoop= GameLoop(gameView as GameView)
        gameView?.loop = gameLoop

        btPause = findViewById(R.id.bt_Pause)
        scoreView = findViewById(R.id.score)

        score = gameView?.score

        scoreView?.setTextColor(Color.WHITE)

    }

    /*
    fun setPause(vue:View){
        if (gameView?.loop?.isRunning() as Boolean) gameView?.loop?.setRunning(false)
        else {
            gameView?.loop?.setRunning(true)
            gameView?.loop?.start()
        }
    }
    */

    override fun onPause() {
        gameView?.loop?.setRunning(false)
        score = gameView?.score

        super.onPause()
    }

    override fun onDestroy() {
        gameView?.loop?.setRunning(false)
        score = gameView?.score

        super.onDestroy()
    }
}