package com.krshubham.mealsonwheels.ui

import android.os.Bundle
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.krshubham.mealsonwheels.R
import kotlinx.android.synthetic.main.activity_ytplayer.*

class YTPlayer : YouTubeBaseActivity() {


    private lateinit var listener: YouTubePlayer.OnInitializedListener
    private lateinit var ytPlayerView : YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ytplayer)
        setActionBar(toolbar)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)

        ytPlayerView = youtube_player


        listener = YtListener()
        ytPlayerView.initialize(getString(R.string.google_api_key), listener)
    }


    class YtListener : YouTubePlayer.OnInitializedListener{

        override fun onInitializationSuccess(
            p0: YouTubePlayer.Provider?,
            p1: YouTubePlayer?,
            p2: Boolean
        ) {

            val list = listOf("W4hTJybfU7s","3LiubyYpEUk")
            p1?.loadVideos(list)

        }

        override fun onInitializationFailure(
            p0: YouTubePlayer.Provider?,
            p1: YouTubeInitializationResult?
        ) {


        }
    }

}


