package com.example.musicapp.fragments.library

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.musicapp.FragmentStack
import com.example.musicapp.R
import com.example.musicapp.newLogic.album.Album

class LibraryFragmentListAdapter(
    private val context: FragmentActivity,
    private val albumsList: MutableList<Any>
) : ArrayAdapter<Any>(context, R.layout.fragment_library_list_item, albumsList) {
    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = context.layoutInflater
        val rowView = inflater.inflate(
            R.layout.fragment_library_list_item,
            null, true
        )

        val albumView = albumsList[position]

        //if(albumView)

        //val imageView = rowView.findViewById<ImageView>(R.id.library_list_view_item_image)
        //imageView.setImageResource(button.icon)
        val textView = rowView.findViewById<TextView>(R.id.library_list_view_item_image)
        //textView.text = albumView

//        rowView.setOnClickListener {
//            //FragmentStack.mainStack.push(button.fragment)
//            val fragmentTransaction = context.supportFragmentManager.beginTransaction()
//            fragmentTransaction
//                .replace(R.id.main_frame, FragmentStack.mainStack.peek())
//                .setReorderingAllowed(true)
//                .addToBackStack("")
//                .commit()
//        }

        return rowView
    }
}
