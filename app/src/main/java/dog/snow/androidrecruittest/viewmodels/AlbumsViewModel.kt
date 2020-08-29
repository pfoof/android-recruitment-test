package dog.snow.androidrecruittest.viewmodels

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import dog.snow.androidrecruittest.repository.CollectionToTreeMap
import dog.snow.androidrecruittest.repository.model.RawAlbum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AlbumsViewModel: CoroutineViewModel() {

    private var albums: MutableLiveData<Map<Int, RawAlbum>> = MutableLiveData()

    fun prepareAlbums(intent: Intent) {
        val albumsJob = async(Dispatchers.IO) {
            val _albums = intent.getParcelableArrayExtra("albums") ?: arrayOf()
            CollectionToTreeMap.getAlbumTree(_albums.mapNotNull { it as RawAlbum })
        }

        launch {
            albums.value = albumsJob.await()
        }
    }
}