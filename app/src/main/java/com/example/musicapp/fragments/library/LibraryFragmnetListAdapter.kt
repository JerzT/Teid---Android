package com.example.musicapp.fragments.library

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
    private val context: Context?,
    private var artistList: List<Artist>
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

        holder.artistName.text = artist.name
        val cover = albumsCovers[artist.coverAlbum]
        if(cover == null){
            cacheAlbumCover(
                artist.coverAlbum,
                artist.cover,
                context
                )
        }
        holder.cover.setImageBitmap(cover)
    }

    @SuppressLint("CheckResult")
    override fun getItemCount(): Int {
        return artistList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: MutableList<Artist>){
        artistList = newList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val artistName: TextView = itemView.findViewById(R.id.library_list_view_item_text)
        val cover: ImageView = itemView.findViewById(R.id.library_list_view_item_image)
    }
}