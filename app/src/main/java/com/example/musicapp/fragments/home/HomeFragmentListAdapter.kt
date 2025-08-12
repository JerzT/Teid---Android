package com.example.musicapp.fragments.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.musicapp.R

class HomeFragmentListAdapter(
    private val context: FragmentActivity,
    private val buttons: Array<HomeButton>
): ArrayAdapter<HomeButton>(context, R.layout.fragment_home_list_item, buttons) {
    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = context.layoutInflater
        val rowView = inflater.inflate(
            R.layout.fragment_home_list_item,
            null, true
        )

        val button = buttons[position]

        val imageView = rowView.findViewById<ImageView>(R.id.home_list_view_item_image)
        imageView.setImageResource(button.icon)
        val textView = rowView.findViewById<TextView>(R.id.home_list_view_item_text)
        textView.text = button.text

        return rowView
    }
}