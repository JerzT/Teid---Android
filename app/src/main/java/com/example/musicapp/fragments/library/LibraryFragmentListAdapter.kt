package com.example.musicapp.fragments.library

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.logic.album.Album
import com.example.musicapp.logic.images.albumsCovers

class LibraryFragmentListAdapter(
    private val albumsList: MutableList<Any>
) : RecyclerView.Adapter<LibraryFragmentListAdapter.ViewHolder>() {

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
        val album = albumsList[position]

        when(album){
            is Album -> {
                holder.albumTitle.text = album.name
                val cover = albumsCovers[album.uri]
                holder.albumCover.setImageBitmap(cover)
            }
            is List<*> -> {
                Log.v("test1", album.toString())
                val usedAlbum = (album as List<Album>)[0]
                holder.albumTitle.text = usedAlbum.name
                val cover = albumsCovers[usedAlbum.uri]
                holder.albumCover.setImageBitmap(cover)
            }
        }

    }

    override fun getItemCount(): Int {
        return albumsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumTitle: TextView = itemView.findViewById(R.id.library_list_view_item_text)
        val albumCover: ImageView = itemView.findViewById(R.id.library_list_view_item_image)
    }
}
