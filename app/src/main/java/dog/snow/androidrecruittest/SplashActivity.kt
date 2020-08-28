package dog.snow.androidrecruittest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dog.snow.androidrecruittest.extensions.getAlbumsAsync
import dog.snow.androidrecruittest.extensions.getPhotosAsync
import dog.snow.androidrecruittest.extensions.getUsersAsync
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
import java.net.ConnectException
import java.net.SocketTimeoutException

class SplashActivity : AppCompatActivity(R.layout.splash_activity) {

    val photoService: PhotoService by inject()
    val userService: UserService by inject()
    val albumService: AlbumService by inject()

    /*
    * Let's keep them globally so if one of them fails, we don't redownload from the
    * beginning
    * */
    private var photos: List<RawPhoto>? = null
    private var albums: MutableList<RawAlbum>? = null
    private var users: MutableList<RawUser>? = null
    private var albumIdsToDownload: Set<Int>? = null
    private var userIdsToDownload: Set<Int>? = null
    
    var coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tryDownload()
    }

    private fun tryDownload() {
        if(!hasNetwork) {
            showError("All networks are down")
            return
        }

        coroutineScope = CoroutineScope(Dispatchers.IO)

        coroutineScope.launch {
            try {
                val tmpPhotos = photos ?: getPhotosAsync().await()

                // Post download check
                if (tmpPhotos == null) {
                    cancel("Received no photos!")
                    return@launch
                }

                photos = tmpPhotos // save photos for next launch

                val albumsIds = albumIdsToDownload ?: SetGenerator.getAlbumIdsFromPhotos(tmpPhotos)
                val tmpAlbums = getAlbumsAsync(albumsIds).await()
                // Post download check
                if (tmpAlbums.any { a -> a == null }) {

                    // Filter out albums that are downloaded
                    albumIdsToDownload =
                        albumsIds.filter { id -> !tmpAlbums.any { a -> a != null && a.id == id } }
                            .toSet()
                    if (albums == null)
                        albums = tmpAlbums.filterNotNull().toMutableList()
                    else
                        albums?.addAll(tmpAlbums.filterNotNull())

                    cancel("Some albums not received!")
                    return@launch
                }

                if (albums == null)
                    albums = tmpAlbums.filterNotNull().toMutableList()
                else
                    albums?.addAll(tmpAlbums.filterNotNull())

                // Filter not null shouldn't happen
                val userIds = userIdsToDownload
                    ?: SetGenerator.getUserIdsFromAlbums(tmpAlbums.filterNotNull())
                val tmpUsers = getUsersAsync(userIds).await()

                if (tmpUsers.any { u -> u == null }) {

                    // Filter out users that are downloaded
                    userIdsToDownload =
                        userIds.filter { id -> !tmpUsers.any { u -> u != null && u.id == id } }
                            .toSet()
                    if (users == null)
                        users = tmpUsers.filterNotNull().toMutableList()
                    else
                        users?.addAll(tmpUsers.filterNotNull())

                    cancel("Some users not received!")
                    return@launch
                }

                if (users == null)
                    users = tmpUsers.filterNotNull().toMutableList()
                else
                    users?.addAll(tmpUsers.filterNotNull())

                runOnUiThread {
                    launchMainActivity()
                }
            } catch (e: SocketTimeoutException) {
                runOnUiThread { showError(e.message) }
            } catch (e:CancellationException) {
                runOnUiThread { showError(e.message) }
            } catch (e: ConnectException) {
                runOnUiThread { showError(e.message) }
            }
        }
    }




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

    private fun launchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("photos", photos!!.toTypedArray())
        intent.putExtra("albums", albums!!.toTypedArray())
        intent.putExtra("users", users!!.toTypedArray())
        startActivity(intent)
    }
}