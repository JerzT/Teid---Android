package com.example.musicapp.fragments.library

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
import com.example.musicapp.logic.artist.artistList

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
    private val albumsList: List<Artist>
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
        val artist = albumsList[position]

        holder.artistName.text = artist.name
    }

    override fun getItemCount(): Int {
        return artistList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val artistName: TextView = itemView.findViewById(R.id.library_list_view_item_text)
        val cover: ImageView = itemView.findViewById(R.id.library_list_view_item_image)
    }
}