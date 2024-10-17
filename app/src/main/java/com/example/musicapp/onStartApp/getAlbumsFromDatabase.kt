import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.example.musicapp.musicFilesUsage.Album
import com.example.musicapp.musicFilesUsage.DBHelper
import com.example.musicapp.musicFilesUsage.setUpDatabase

@SuppressLint("Range")
fun getAlbumsFromDatabase(
    context: Context,
): MutableList<Album> {
    val database = setUpDatabase(context)
    //get saved albums
    val albumsList: MutableList<Album> = mutableListOf()
    val getAlbumResult = database.getAlbums()
    if(getAlbumResult != null){
        getAlbumResult.use { albumRow ->
            while (albumRow.moveToNext()){
                val name: String? = albumRow.getString(albumRow.getColumnIndex("name")).takeIf { it.isNotEmpty() }
                val uri: Uri = albumRow.getString(albumRow.getColumnIndex("uri")).toUri()
                val cover: Uri? = albumRow.getString(albumRow.getColumnIndex("cover")).takeIf { it.isNotEmpty() }?.toUri()
                val artist: String? = albumRow.getString(albumRow.getColumnIndex("artist")).takeIf { it.isNotEmpty() }
                val year: String? = albumRow.getString(albumRow.getColumnIndex("year")).takeIf { it.isNotEmpty() }
                val cdNumber: Int = albumRow.getInt(albumRow.getColumnIndex("cd_number"))

                val album = Album(
                    name = name,
                    uri = uri,
                    cover = cover,
                    artist = artist,
                    year = year,
                    cdNumber = cdNumber,
                )

                albumsList += album
            }
        }
        getAlbumResult.close()
    }
    return albumsList
}