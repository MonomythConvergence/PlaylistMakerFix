package com.example.playlistmaker


import android.content.Intent
import android.util.TypedValue
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
    val debounce = Debounce()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_search_result_item, parent, false)
        val holder = TrackViewHolder(view, activityInstance)
        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val playerIntent = Intent(activityInstance, PlayerActivity::class.java)
            if (debounce.clickDebounce()) {
                playerIntent.putExtra(Constants.PARCELABLE_TO_PLAYER_KEY, list[position])
                if (position != RecyclerView.NO_POSITION) {
                    val track = list[position]
                    SearchHistory(App.recentTracksSharedPreferences).addTrackToRecent(track)
                    activityInstance.startActivity(playerIntent)
                }
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = list[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int = list.size

    class TrackViewHolder(itemView: View, activity: SearchActivity) :
        RecyclerView.ViewHolder(itemView) {
        private val trackName: TextView = itemView.findViewById(R.id.track_name)
        private val artistName: TextView = itemView.findViewById(R.id.artist_name)
        private val trackTime: TextView = itemView.findViewById(R.id.track_time)
        private val artworkImageView: ImageView = itemView.findViewById(R.id.track_artwork)

        fun bind(track: Track) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = track.trackTime
            val roundingRadius = itemView.resources.getDimension(R.dimen.Track_icon_rounding)
            val pixelsForRoundedCorners = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX,
                roundingRadius,
                itemView.resources.displayMetrics
            )
            Glide.with(artworkImageView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .transform(CenterCrop(), RoundedCorners(pixelsForRoundedCorners.toInt()))
                .into(artworkImageView)
        }

    }

}

