package e.pop.pongchallenge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*

class GameActivity:AppCompatActivity(){

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)

        //set FullScreen
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //Set no Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //  setContentView(R.layout.game_main)
    }


}