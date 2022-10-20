package com.example.jankenponpon

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class Game(val activity : AppCompatActivity) : Thread()
{
    private val mp = MediaPlayer()
    private val mp1 = MediaPlayer()
    private val packageName : String = activity.packageName
    private val applicationContext : Context = activity.applicationContext
    private val rockBitmap : Bitmap = BitmapFactory.decodeResource(activity.resources, R.drawable.rock)
    private val paperBitmap : Bitmap = BitmapFactory.decodeResource(activity.resources, R.drawable.paper)
    private val scissorsBitmap : Bitmap = BitmapFactory.decodeResource(activity.resources, R.drawable.scissors)
    private val clearBitmap : Bitmap = BitmapFactory.decodeResource(activity.resources, R.drawable.blank)
    var canSetHand = true

    fun initializeGame() {

        activity.cpuHandImage.setImageBitmap(clearBitmap)
        var winCounter = 0;
        activity.runOnUiThread {
            activity.resultView.text = winCounter.toString()
        }


        playAudio("start_audio")
        while(mp.isPlaying){ Thread.sleep(10)}

        var shouldPlay = true
        while(shouldPlay) {
            playAudio("janken_ponpon_audio")

            val rand =  Random()
            val handInt = rand.nextInt(3)

            var currentTime = mp.currentPosition
            //wait for first pon to display cpu hand
            while(currentTime < 1250) {
                currentTime = mp.currentPosition
                Thread.sleep(10)
            }
            var cpuHand = HAND_TYPE.NO_HAND
            when (handInt) {
                0 -> {
                    cpuHand = HAND_TYPE.ROCK
                    activity.cpuHandImage.setImageBitmap(rockBitmap)
                }
                1 -> {
                    cpuHand = HAND_TYPE.PAPER
                    activity.cpuHandImage.setImageBitmap(paperBitmap)
                }
                2 -> {
                    cpuHand = HAND_TYPE.SCISSORS
                    activity.cpuHandImage.setImageBitmap(scissorsBitmap)
                }
            }

            while(currentTime < 1600) {
                currentTime = mp.currentPosition
                Thread.sleep(10)
            }

            canSetHand = true;
            var playerHand = HAND_TYPE.NO_HAND
            val startTime = System.currentTimeMillis()
            var currentTimeDetached : Long = currentTime.toLong()
            while(currentTimeDetached < 2200) {
                playerHand = (activity as MainActivity).playerHand
                currentTimeDetached = currentTime + (System.currentTimeMillis() - startTime)

                Thread.sleep(10)
            }
            canSetHand = false;

            if (cpuHand == HAND_TYPE.ROCK && playerHand == HAND_TYPE.SCISSORS)
            {
                winCounter++;
                playScoreSound()
            }
            else if (cpuHand == HAND_TYPE.PAPER && playerHand == HAND_TYPE.ROCK)
            {
                winCounter++;
                playScoreSound()
            }
            else if (cpuHand == HAND_TYPE.SCISSORS && playerHand == HAND_TYPE.PAPER)
            {
                winCounter++;
                playScoreSound()
            }
            else shouldPlay = false

            activity.runOnUiThread {
                activity.resultView.text = winCounter.toString()
            }
            (activity as MainActivity).playerHand = HAND_TYPE.NO_HAND
            activity.cpuHandImage.setImageBitmap(clearBitmap)
        }

    }

    private fun playAudio(audioName : String)
    {
        mp.reset()
        try {
            mp.setDataSource(applicationContext , Uri.parse("android.resource://" + packageName + "/raw/" + audioName))
            mp.prepare()
            mp.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playScoreSound()
    {
        mp1.reset()
        try {
            mp1.setDataSource(applicationContext , Uri.parse("android.resource://" + packageName + "/raw/score_sound"))
            mp1.prepare()
            mp1.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}