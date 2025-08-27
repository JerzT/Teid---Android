package com.example.musicapp.fragments.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.FragmentStack
import com.example.musicapp.R
import com.example.musicapp.logic.album.Album
import com.example.musicapp.logic.album.albumsList

class LibraryFragment: Fragment() {
    private lateinit var libraryRecyclerView: RecyclerView
    private lateinit var flattenAlbumList: MutableList<Album>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        flattenAlbumList = mutableListOf<Album>()
        for( album in albumsList){
            when(album){
                is Album -> flattenAlbumList.add(album)
                is List<*> -> flattenAlbumList.add((album as List<Album>)[0])
                else -> null
            }
        }
        return inflater.inflate(
            R.layout.fragment_library,
            container,
            false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val returnButton: ImageButton = view.findViewById(R.id.fragment_library_back)

        returnButton.setOnClickListener {
            FragmentStack.mainStack.pop()
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction
                .replace(R.id.main_frame, FragmentStack.mainStack.peek())
                .setReorderingAllowed(true)
                .addToBackStack("")
                .commit()
        }

        libraryRecyclerView = view.findViewById(R.id.fragment_library_list_view)
        libraryRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        flattenAlbumList.sortBy { album -> album.name }
        val arrayAdapter = LibraryAllAlbumsFragmentListAdapter(flattenAlbumList)

        libraryRecyclerView.adapter = arrayAdapter
    }
}