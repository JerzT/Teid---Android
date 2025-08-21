package com.example.musicapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicapp.fragments.home.HomeFragment
import com.example.musicapp.newLogic.album.Album
import com.example.musicapp.newLogic.album.connectDiscFromAlbums
import com.example.musicapp.logic.database.getAlbumsFromDatabase
import com.example.musicapp.newLogic.album.synchronizeAlbums
import com.example.musicapp.newLogic.directory.getAlbumsFromDirectory
import com.example.musicapp.logic.mediaPlayer.PlaybackService
import com.example.musicapp.newLogic.settings.SettingsDataStore
import com.example.musicapp.newLogic.DirectoryUri
import com.example.musicapp.newLogic.album.albumsList
import com.example.musicapp.popUps.DirectorySelectPopUp
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
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

        if (savedInstanceState == null){
            FragmentStack.mainStack.push(HomeFragment())

            supportFragmentManager.beginTransaction()
                .add(R.id.main_frame, HomeFragment())
                .commit()
        }

        settings = SettingsDataStore(this)

        //set up notification service
        val name = "Teid"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel("1", name, importance)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)

        //get directory if possible and get albums
        GlobalScope.launch {
            val context = this@MainActivity

            settings?.directoryPathFlow?.collect { directoryPath ->
                DirectoryUri.uri = directoryPath?.toUri() //changeNotValidDirectoryPathToUri(directoryPath)
                val uri = DirectoryUri.uri
                if (uri != null) {
                    val albumsFromDatabase = getAlbumsFromDatabase(context)
                        .apply { sortBy { if (it is Album) it.name else ""  } }
                    val connectedAlbumsFromDatabase = connectDiscFromAlbums(albumsFromDatabase)

                    albumsList.addAll(connectedAlbumsFromDatabase)
                    //Log.v("test1", "ok")
                    //cacheAlbumCovers(connectedAlbumsFromDatabase, context)

                    val albumsInDirectory = getAlbumsFromDirectory(
                        settingsDataStore = settings!!,
                        context = context,
                    ).apply { sortBy { if (it is Album) it.name else ""  } }
                    val connectedAlbumsFromDirectory = connectDiscFromAlbums(albumsInDirectory)

                    albumsList.clear()
                    albumsList.addAll(connectedAlbumsFromDirectory)
                    //Log.v("test1", "ok2")
                    //cacheAlbumCovers(connectedAlbumsFromDirectory, context)

                    synchronizeAlbums(
                        albumsFromDatabase = albumsFromDatabase,
                        albumsInDirectory = albumsInDirectory,
                        context = context,
                    )
                    //Log.v("test1", "ok3")
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

        //val chooseDirectory = this.findViewById<Button>(R.id.choose_directory)
    }

    override fun onDestroy() {
        val notificationManager = baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.deleteNotificationChannel("1")

        super.onDestroy()
    }
}