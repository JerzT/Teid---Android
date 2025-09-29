package com.example.musicapp.fragments.library

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.logic.artist.Artist
import com.example.musicapp.logic.images.albumsCovers
import com.example.musicapp.logic.images.cacheAlbumCover

class ArtistDiffCallback(): DiffUtil.ItemCallback<Artist>(){
    override fun areItemsTheSame(
        oldItem: Artist,
        newItem: Artist
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(
        oldItem: Artist,
        newItem: Artist
    ): Boolean {
        TODO("Not yet implemented")
    }

}

class LibraryFragmentListAdapter(
    private val context: Context,
    private var artistList: List<Any>
) : ListAdapter<Artist, LibraryFragmentListAdapter.ViewHolder>(ArtistDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_library_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val artist = artistList[position]
        when(artist){
            is Artist -> {
                holder.artistName.text = artist.name
                val cover = albumsCovers[artist.coverAlbum]
                // if cover is returned null try getting cover
                // some of the artist get link to second disk of the album
                // and it not cached when all covers are
                if(cover == null){
                    cacheAlbumCover(
                        artist.coverAlbum,
                        artist.cover,
                        context
                    )
                }
                holder.cover.setImageBitmap(cover)
                holder.arrow.visibility = View.VISIBLE
                holder.cover.visibility = View.VISIBLE
                val layoutParams = holder.cover.layoutParams
                layoutParams.width = dpToPx(32, context)
                layoutParams.height = dpToPx(32, context)
                holder.cover.layoutParams = layoutParams
            }
            is String -> {
                holder.artistName.text = artist
                holder.arrow.visibility = View.INVISIBLE
                holder.cover.visibility = View.INVISIBLE
                val layoutParams = holder.cover.layoutParams
                layoutParams.width = 0
                layoutParams.height = 0
                holder.cover.layoutParams = layoutParams
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun getItemCount(): Int {
        return artistList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: MutableList<Any>){
        artistList = newList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val artistName: TextView = itemView.findViewById(R.id.library_list_view_item_text)
        val cover: ImageView = itemView.findViewById(R.id.library_list_view_item_image)
        val arrow: ImageView = itemView.findViewById(R.id.library_list_view_item_arrow)
    }
}

private fun dpToPx(dp: Int, context: Context): Int {
    return (dp * context.resources.displayMetrics.density).toInt()
}