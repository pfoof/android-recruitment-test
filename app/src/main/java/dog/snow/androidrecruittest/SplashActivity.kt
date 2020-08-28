package dog.snow.androidrecruittest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dog.snow.androidrecruittest.extensions.hasNetwork
import dog.snow.androidrecruittest.repository.Endpoint
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.service.PhotoService
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity(R.layout.splash_activity) {

    private val photoService: PhotoService by inject()
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
            val photos = getPhotosAsync().await()

        }
    }

    private fun getPhotosAsync(): Deferred<List<RawPhoto>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async photoService.getPhotos(100).execute().body()
        }

    private fun showError(errorMessage: String?) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.cant_download_dialog_title)
            .setMessage(getString(R.string.cant_download_dialog_message, errorMessage))
            .setPositiveButton(R.string.cant_download_dialog_btn_positive) { _, _ -> /*tryAgain()*/ }
            .setNegativeButton(R.string.cant_download_dialog_btn_negative) { _, _ -> finish() }
            .create()
            .apply { setCanceledOnTouchOutside(false) }
            .show()
    }
}