package com.example.jankenponpon

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import android.R.attr.start
import java.io.File.separator
import android.R.attr.path
import android.media.MediaPlayer
import android.net.Uri
import java.io.File
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private lateinit var game : Game
    public var playerHand = HAND_TYPE.NO_HAND

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)
        game = Game(this)
    }

    fun onBtnClicked(v : View)
    {
        if(v.id == startBtn.id)
        {
            thread {
                game.initializeGame()
            }

        }
    }

    fun onRockClicked(v : View)
    {
        if (game.canSetHand) playerHand = HAND_TYPE.ROCK
    }

    fun onPaperClicked(v : View)
    {
        if (game.canSetHand) playerHand = HAND_TYPE.PAPER
    }

    fun onScissorsClicked(v : View)
    {
        if (game.canSetHand) playerHand = HAND_TYPE.SCISSORS
    }


}
