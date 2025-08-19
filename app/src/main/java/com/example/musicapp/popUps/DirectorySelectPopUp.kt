package com.example.musicapp.popUps

import android.content.DialogInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.example.musicapp.R
import com.example.musicapp.logic.album.findAlbums
import com.example.musicapp.logic.settings.SettingsDataStore
import com.example.musicapp.logic.settings.settings
import com.example.musicapp.newLogic.DirectoryUri
import com.example.musicapp.newLogic.albumsList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class DirectorySelectPopUp: DialogFragment() {
    private lateinit var settingsDataStore: SettingsDataStore

    //delegate
    private var myUri: Uri? by Delegates.observable(DirectoryUri.uri) { property, oldValue, newValue ->
        if (DirectoryUri.uri != null){
            this.isCancelable = true
            GlobalScope.launch {
                settingsDataStore.saveDirectoryPath(DirectoryUri.uri.toString())
                findAlbums(
                    uri = DirectoryUri.uri,
                    context = requireActivity(),
                    albumsList = albumsList
                ).await()
                //Log.v("test1", albumsList.toString())
            }
            this.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.popup_directory_select,
            container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsDataStore = SettingsDataStore(requireContext())

        val buttonChooseDirectory = view.findViewById<Button>(R.id.popup_directory_select_button)
        buttonChooseDirectory.setOnClickListener {
            getContent.launch("".toUri())
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.P)
    val getContent = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
        DirectoryUri.uri = uri
        myUri = uri
    }
}