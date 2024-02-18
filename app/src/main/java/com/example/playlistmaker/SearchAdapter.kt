package com.example.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.ArrayList

class SearchAdapter(diplayedList: ArrayList<Track>, activity: SearchActivity) :


    RecyclerView.Adapter<SearchAdapter.TrackViewHolder>() {
    val list = diplayedList
    val activityInstance = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_search_result_item, parent, false)
        val holder = TrackViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val playerIntent = Intent(activityInstance, PlayerActivity::class.java)
            playerIntent.putExtra("selectedTrack", list[position])
            if (position != RecyclerView.NO_POSITION) {
                val track = list[position]
                SearchHistory(App.recentTracksSharedPreferences).addTrackToRecent(track)
            }
            activityInstance.startActivity(playerIntent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = list[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int = list.size

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView = itemView.findViewById(R.id.track_name)
        private val artistName: TextView = itemView.findViewById(R.id.artist_name)
        private val trackTime: TextView = itemView.findViewById(R.id.track_time)
        private val artworkImageView: ImageView = itemView.findViewById(R.id.track_artwork)

        fun bind(track: Track) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = track.trackTime
            Glide.with(artworkImageView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .transform(CenterCrop(), RoundedCorners(R.dimen.Track_icon_rounding))
                .into(artworkImageView)
        }
    }

}

