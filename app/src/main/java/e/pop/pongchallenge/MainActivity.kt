package e.pop.pongchallenge

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    var gameView:GameView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView=GameView(this)

        setContentView(gameView)
    }
}
