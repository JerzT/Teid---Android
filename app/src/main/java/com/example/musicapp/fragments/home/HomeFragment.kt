package com.example.musicapp.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.musicapp.R

class HomeFragment: Fragment() {
    private lateinit var homeListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayAdapter: ArrayAdapter<*>
        val buttons = arrayOf(
            HomeButton(R.drawable.baseline_library_music_24, "Library"),
            HomeButton(R.drawable.baseline_playlist, "Playlists"),
            HomeButton(R.drawable.baseline_radio, "Internet Radio"),
            HomeButton(R.drawable.baseline_chart, "Stats"),
            HomeButton(R.drawable.baseline_settings_24, "Settings"),
            HomeButton(R.drawable.baseline_info, "About")
        )

        homeListView = view.findViewById(R.id.home_list_view)
        arrayAdapter = ArrayAdapter(requireActivity(), R.layout.fragment_home_list_item, buttons)
        homeListView.adapter = arrayAdapter
    }
}

