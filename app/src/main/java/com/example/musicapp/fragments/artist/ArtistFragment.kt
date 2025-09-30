package com.example.musicapp.fragments.artist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.musicapp.FragmentStack
import com.example.musicapp.R

class ArtistFragment: Fragment()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_artist_view,
            container,
            false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val returnButton: ImageButton = view.findViewById(R.id.fragment_artist_view_back)

        returnButton.setOnClickListener {
            FragmentStack.mainStack.pop()
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction
                .replace(R.id.main_frame, FragmentStack.mainStack.peek())
                .setReorderingAllowed(true)
                .addToBackStack("")
                .commit()
        }
    }
}
