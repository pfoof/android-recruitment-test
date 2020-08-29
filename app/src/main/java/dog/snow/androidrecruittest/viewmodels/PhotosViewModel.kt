package dog.snow.androidrecruittest.viewmodels

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import dog.snow.androidrecruittest.repository.model.RawPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PhotosViewModel: CoroutineViewModel() {

    private var photos: MutableLiveData<List<RawPhoto>> = MutableLiveData()

    fun preparePhotos(intent: Intent) {
        val photosJob = async(Dispatchers.IO) {
            val _photos = intent.getParcelableArrayExtra("photos") ?: arrayOf()
            _photos.mapNotNull { it as RawPhoto }
        }

        launch {
            photos.value = photosJob.await()
        }
    }

}