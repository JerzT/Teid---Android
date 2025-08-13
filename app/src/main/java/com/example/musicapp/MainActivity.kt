package com.example.musicapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicapp.fragments.home.HomeFragment
import com.example.musicapp.logic.album.findAlbums
import com.example.musicapp.logic.mediaPlayer.PlaybackService
import com.example.musicapp.logic.settings.SettingsDataStore
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

var sessionToken: SessionToken? = null
var controllerFuture: ListenableFuture<MediaController>? = null

class MainActivity : AppCompatActivity() {
    var uri: Uri? = null

    val albumsList: MutableList<Any> = mutableListOf()

    var settings: SettingsDataStore? = null
    @RequiresApi(Build.VERSION_CODES.P)
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
        /*GlobalScope.launch {
            settings.directoryPathFlow.collect { directoryPath ->
                uri = changeNotValidDirectoryPathToUri(directoryPath)
            }

            val context = this@MainActivity

            settings.directoryPathFlow.collect { directoryPath ->
                uri = changeNotValidDirectoryPathToUri(directoryPath)
                if (uri != null) {

                    val albumsFromDatabase = getAlbumsFromDatabase(context)
                        .apply { sortBy { if (it is Album) it.name else ""  } }
                    val connectedAlbumsFromDatabase = connectDiscFromAlbums(albumsFromDatabase)

                    albumsList.addAll(connectedAlbumsFromDatabase)
                    cacheAlbumCovers(connectedAlbumsFromDatabase, context)

                    val albumsInDirectory = getAlbumsFromDirectory(
                        context = context,
                        uri = uri
                    ).apply { sortBy { if (it is Album) it.name else ""  } }
                    val connectedAlbumsFromDirectory = connectDiscFromAlbums(albumsInDirectory)

                    albumsList.clear()
                    albumsList.addAll(connectedAlbumsFromDirectory)
                    cacheAlbumCovers(connectedAlbumsFromDirectory, context)

                    synchronizeAlbums(
                        albumsFromDatabase = albumsFromDatabase,
                        albumsInDirectory = albumsInDirectory,
                        context = context,
                    )
                }
            }
        }*/

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)){ v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //set up session of playback
        sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(this, sessionToken!!).buildAsync()

        //val chooseDirectory = this.findViewById<Button>(R.id.choose_directory)

/*        chooseDirectory.setOnClickListener {
            getContent.launch("".toUri())
        }*/
    }

    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.P)
    val getContent = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
        this.uri = uri
        GlobalScope.launch {
            //settings!!.saveDirectoryPath(uri.toString())

            findAlbums(
                uri = uri,
                context = this@MainActivity,
                albumsList = albumsList
            ).await()

            //Log.v("test1", albumsList.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        val notificationManager = baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.deleteNotificationChannel("1")

        super.onDestroy()
    }
}