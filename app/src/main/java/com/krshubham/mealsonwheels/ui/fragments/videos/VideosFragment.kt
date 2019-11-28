package com.krshubham.mealsonwheels.ui.fragments.videos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.YouTubeService
import com.krshubham.mealsonwheels.adapter.VideoAdapter
import com.krshubham.mealsonwheels.response.Item
import com.krshubham.mealsonwheels.response.SearchResponse
import com.krshubham.mealsonwheels.utils.ScopedFragment
import kotlinx.android.synthetic.main.fragment_videos.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideosFragment : ScopedFragment() {

    private lateinit var videosViewModel: VideosViewModel
    private lateinit var adapter: VideoAdapter

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


        val searchResults = YouTubeService().getVideos()
        var results: List<Item>

        searchResults.enqueue(object: Callback<SearchResponse>{
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Toast.makeText(this@VideosFragment.context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {

                results = response.body()?.items!!

                val layoutManager = GridLayoutManager(this@VideosFragment.context, 2)
                adapter = VideoAdapter(
                    this@VideosFragment.context!!,
                    results
                )
                video_recycler_view.adapter = adapter
                video_recycler_view.layoutManager = layoutManager
            }


        })




    }


}