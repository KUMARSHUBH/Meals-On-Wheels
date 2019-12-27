package com.krshubham.mealsonwheels.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.krshubham.mealsonwheels.R
import com.krshubham.mealsonwheels.response.Item
import com.krshubham.mealsonwheels.ui.YTPlayer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.youtube_playlist_item.view.*

class VideoAdapter(val context: Context,
                   val results : List<Item>): RecyclerView.Adapter<VideoAdapter.ViewHolder>() {



    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        var image = view.thumbnail
        var title = view.title
        var play = view.play_button

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(context).inflate(R.layout.youtube_playlist_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(results[position].snippet.thumbnails.medium.url).placeholder(R.drawable.food_background).into(holder.image)
        holder.image.clipToOutline = true
        holder.title.text = results[position].snippet.title

        holder.play.setOnClickListener {

            val intent = Intent(context, YTPlayer::class.java)
            intent.putExtra("videoId", results[position].id.videoId)
            intent.putExtra("title",results[position].snippet.title)
            intent.putExtra("desc",results[position].snippet.description)
            context.startActivity(intent)
        }
    }


}
