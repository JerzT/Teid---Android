package com.example.musicapp.fragments.library

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.FragmentStack
import com.example.musicapp.R

class LibraryFragment: Fragment() {
    private lateinit var libraryRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

    @SuppressLint("NotifyDataSetChanged")
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
        val arrayAdapter = LibraryFragmentListAdapter( context,
            LibraryLiveViewModel
                .artistSet
                .value!!
                .toMutableList()
        )
        libraryRecyclerView.adapter = arrayAdapter

        LibraryLiveViewModel.artistSet.observe(viewLifecycleOwner) { artists ->
            arrayAdapter.updateData(artists.sortedBy { it.name }.toMutableList())
        }
        //flattenAlbumList.sortBy { album -> album.name }
    }
}