package com.example.musicapp.fragments.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.FragmentStack
import com.example.musicapp.R
import com.example.musicapp.fragments.home.HomeFragmentListAdapter
import com.example.musicapp.newLogic.album.albumsList

class LibraryFragment: Fragment() {
    private lateinit var libraryListView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        libraryListView = view.findViewById(R.id.fragment_library_list_view)
        libraryListView.layoutManager = LinearLayoutManager(requireActivity())
        val arrayAdapter = LibraryFragmentListAdapter(albumsList)
        libraryListView.adapter = arrayAdapter
    }

}