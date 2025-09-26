package com.example.musicapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicapp.fragments.home.HomeFragment
import com.example.musicapp.fragments.library.LibraryLiveViewModel
import com.example.musicapp.logic.album.Album
import com.example.musicapp.logic.album.connectDiscFromAlbums
import com.example.musicapp.logic.database.getAlbumsFromDatabase
import com.example.musicapp.logic.album.synchronizeAlbums
import com.example.musicapp.logic.directory.getAlbumsFromDirectory
import com.example.musicapp.logic.mediaPlayer.PlaybackService
import com.example.musicapp.logic.settings.SettingsDataStore
import com.example.musicapp.logic.DirectoryUri
import com.example.musicapp.logic.album.albumsList
import com.example.musicapp.logic.database.getArtistsFromDatabase
import com.example.musicapp.popUps.DirectorySelectPopUp
import com.example.musicapp.logic.images.cacheAlbumsCovers
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

var sessionToken: SessionToken? = null
var controllerFuture: ListenableFuture<MediaController>? = null

class MainActivity : AppCompatActivity() {

    var settings: SettingsDataStore? = null
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //set activity main as main view
        setContentView(R.layout.activity_main)

        //set up notification service
        val name = "Teid"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel("1", name, importance)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)

        //start managing stack of fragments
        if (savedInstanceState == null){
            FragmentStack.mainStack.push(HomeFragment())

            supportFragmentManager.beginTransaction()
                .add(R.id.main_frame, HomeFragment())
                .commit()
        }

        //set up stored settings
        settings = SettingsDataStore(this)

        //get directory if possible and get albums
        //and check if state from database is still actually
        GlobalScope.launch {
            val context = this@MainActivity

            //check if directory is stored
            settings?.directoryPathFlow?.collect { directoryPath ->
                DirectoryUri.uri = directoryPath?.toUri() //changeNotValidDirectoryPathToUri(directoryPath)
                if (DirectoryUri.uri != null) {

                    val artistsFromDatabase = getArtistsFromDatabase(context)
                    LibraryLiveViewModel.setArtistSet(artistsFromDatabase)

                    val albumsFromDatabase = getAlbumsFromDatabase(context)
                        .apply { sortBy { if (it is Album) it.name else ""  } }
                    //connectedAlbums must stay because we need to synchronize
                    //albums from database and found one
                    val connectedAlbumsFromDatabase = connectDiscFromAlbums(albumsFromDatabase)

                    connectedAlbumsFromDatabase.sortedBy {
                        when(it){
                            is Album -> { it.name?.lowercase() }
                            is List<*> -> { (it as List<Album>)[0].name?.lowercase()}
                            else -> TODO()
                    }}
                    albumsList.addAll(connectedAlbumsFromDatabase)

                    coroutineScope {
                        launch {
                            cacheAlbumsCovers(connectedAlbumsFromDatabase, context)
                        }
                    }

                    val albumsInDirectory = getAlbumsFromDirectory(
                        settingsDataStore = settings!!,
                        context = context,
                    ).apply { sortBy { if (it is Album) it.name else ""  } }
                    val connectedAlbumsFromDirectory = connectDiscFromAlbums(albumsInDirectory)

                    albumsList.clear()
                    albumsList.addAll(connectedAlbumsFromDirectory)
                    coroutineScope {
                        launch {
                            cacheAlbumsCovers(connectedAlbumsFromDirectory, context)
                        }
                    }

                    synchronizeAlbums(
                        albumsFromDatabase = albumsFromDatabase,
                        albumsInDirectory = albumsInDirectory,
                        context = context,
                    )
                }
                else{
                    val directorySelectPopUp = DirectorySelectPopUp()
                    directorySelectPopUp.show(supportFragmentManager, "")
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)){ v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //set up session of playback
        sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(this, sessionToken!!).buildAsync()
    }

    override fun onDestroy() {
        val notificationManager = baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.deleteNotificationChannel("1")

        super.onDestroy()
    }
}