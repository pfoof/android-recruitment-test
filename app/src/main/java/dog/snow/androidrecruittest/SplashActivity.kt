package dog.snow.androidrecruittest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dog.snow.androidrecruittest.extensions.hasNetwork
import dog.snow.androidrecruittest.repository.Endpoint
import dog.snow.androidrecruittest.repository.SetGenerator
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import dog.snow.androidrecruittest.repository.service.AlbumService
import dog.snow.androidrecruittest.repository.service.PhotoService
import dog.snow.androidrecruittest.repository.service.UserService
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity(R.layout.splash_activity) {

    private val photoService: PhotoService by inject()
    private val userService: UserService by inject()
    private val albumService: AlbumService by inject()

    /*
    * Let's keep them globally so if one of them fails, we don't redownload from the
    * beginning
    * */
    private var photos: List<RawPhoto>? = null
    private var albums: MutableList<RawAlbum>? = null
    private var users: MutableList<RawUser>? = null
    private var albumIdsToDownload: Set<Int>? = null

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(parentJob + Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private fun tryDownload() {
        if(!hasNetwork) {
            showError("All networks are down")
            return
        }

        coroutineScope.launch {
            val tmpPhotos = photos ?:
                getPhotosAsync().await()

            // Post download check
            if (tmpPhotos == null) {
                runOnUiThread { showError("Received no photos!") }
                return@launch
            }

            photos = tmpPhotos // save photos for next launch

            val albumsIds = albumIdsToDownload ?: SetGenerator.getAlbumIdsFromPhotos(tmpPhotos)
            val tmpAlbums = getAlbumsAsync(albumsIds).await()
            // Post download check
            if (tmpAlbums.any { a -> a == null }) {

                // Filter out albums that are downloaded
                albumIdsToDownload = albumsIds.filter { id -> !tmpAlbums.any { a -> a != null && a.id == id } }.toSet()
                if(albums == null)
                    albums = tmpAlbums.filterNotNull().toMutableList()
                else
                    albums?.addAll(tmpAlbums.filterNotNull())

                runOnUiThread { showError("Some albums not received!") }
                return@launch
            }

            if(albums == null)
                albums = tmpAlbums.filterNotNull().toMutableList()
            else
                albums?.addAll(tmpAlbums.filterNotNull())


        }
    }

    private fun getPhotosAsync(): Deferred<List<RawPhoto>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async photoService.getPhotos(100).execute().body()
        }

    private fun getUsersAsync(users: Set<Int>): Deferred<Set<RawUser?>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async users.map { u -> getUserAsync(u) }.toSet()
        }

    private fun getUserAsync(id: Int): RawUser?
        = userService.getUser(id).execute().body()

    private fun getAlbumsAsync(albums: Set<Int>): Deferred<Set<RawAlbum?>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async albums.map { a -> getAlbumAsync(a) }.toSet()
        }

    private fun getAlbumAsync(id: Int): RawAlbum?
        = albumService.getAlbum(id).execute().body()


    private fun showError(errorMessage: String?) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.cant_download_dialog_title)
            .setMessage(getString(R.string.cant_download_dialog_message, errorMessage))
            .setPositiveButton(R.string.cant_download_dialog_btn_positive) { _, _ -> tryDownload() }
            .setNegativeButton(R.string.cant_download_dialog_btn_negative) { _, _ -> finish() }
            .create()
            .apply { setCanceledOnTouchOutside(false) }
            .show()
    }

    private fun launchMainActivity(photos: List<RawPhoto>) {
        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
    }
}