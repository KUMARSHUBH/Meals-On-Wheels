package com.krshubham.mealsonwheels.ui.fragments.videos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.ui.YTPlayer
import kotlinx.android.synthetic.main.fragment_videos.*

class VideosFragment : Fragment() {

    private lateinit var videosViewModel: VideosViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        videosViewModel =
            ViewModelProviders.of(this).get(VideosViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_videos, container, false)


        return root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        go.setOnClickListener {

            startActivity(Intent(this.context, YTPlayer::class.java))

        }



    }




}