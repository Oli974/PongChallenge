package e.pop.pongchallenge

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.*

class GameActivity:AppCompatActivity(){

    private var btPause:Button?= null
    private var scoreView:TextView?=null

    private var gameView:GameView?=null
    private var gameLoop:GameLoop?=null

    private var scoreRunnable = Runnable {  }
    private var handler = Handler()
    private var score:Int?=null

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
        scoreView?.text = "Score : "+score

    }


    fun setPause(vue:View){
        if (gameView?.loop?.isRunning() as Boolean) gameView?.loop?.setPause()
    }

    override fun onStart() {
        super.onStart()
        scoreRunnable = object : Runnable{
            override fun run() {
                score = gameView?.getScore()
                scoreView?.text = "Score : $score"
            }
        }
    }


    override fun onPause() {
        super.onPause()
        gameView?.loop?.setRunning(false)
    }

    override fun onStop(){
        super.onStop()
        handler.removeCallbacks(scoreRunnable)
        gameView?.loop?.setRunning(false)
        finish()
    }

    override fun onDestroy() {
        gameView?.loop?.setRunning(false)
        super.onDestroy()
    }
}