package com.example.musicapp.fragments.home

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentActivity
import com.example.musicapp.R

class HomeFragmentListAdapter(
    private val context: FragmentActivity,
    private val buttons: Array<HomeButton>
): ArrayAdapter<HomeButton>(context, R.layout.fragment_home_list_item,) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent)
    }
}