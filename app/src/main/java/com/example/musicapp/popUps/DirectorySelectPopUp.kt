package com.example.musicapp.popUps

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.example.musicapp.R
import com.example.musicapp.logic.directory.getAlbumsFromDirectory
import com.example.musicapp.logic.settings.SettingsDataStore
import com.example.musicapp.newLogic.DirectoryUri
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlin.properties.Delegates

class DirectorySelectPopUp: DialogFragment() {
    private lateinit var settingsDataStore: SettingsDataStore

    //delegate to listen changes of myUri just for the ui purpose
    private var myUri: Uri? by Delegates.observable(DirectoryUri.uri) { property, oldValue, newValue ->
        if (DirectoryUri.uri != null){
            this.isCancelable = true
            getAlbumsFromDirectory(settingsDataStore, requireActivity())
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
            getUriTree.launch("".toUri())
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    val getUriTree = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
        DirectoryUri.uri = uri
        myUri = uri
    }
}