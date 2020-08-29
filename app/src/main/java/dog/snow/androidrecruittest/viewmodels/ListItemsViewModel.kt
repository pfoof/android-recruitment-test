package dog.snow.androidrecruittest.viewmodels

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import dog.snow.androidrecruittest.repository.CollectionToTreeMap
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import dog.snow.androidrecruittest.ui.model.ListItem
import dog.snow.androidrecruittest.ui.model.ListItemGenerator
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class ListItemsViewModel: CoroutineViewModel() {
    var listItems: MutableLiveData<List<ListItem>> = MutableLiveData()
    var photos: MutableLiveData<List<RawPhoto>> = MutableLiveData()
    var users: MutableLiveData<TreeMap<Int, RawUser>> = MutableLiveData()

    fun prepareValuesFromIntent(intent: Intent) {
        prepareListItems(intent)
        prepareUsers(intent)
    }

    private fun prepareUsers(intent: Intent) {
        val usersJob = async(Dispatchers.IO) {
            val _users = intent.getParcelableArrayExtra("users") ?: arrayOf()
            CollectionToTreeMap.getUsersTree(_users.mapNotNull { it as RawUser })
        }
        launch {
            users.value = usersJob.await()
        }
    }

    private fun prepareListItems(intent: Intent) {

        val photosJob = async(Dispatchers.IO) {
            val _photos = intent.getParcelableArrayExtra("photos") ?: arrayOf()
            _photos.mapNotNull { it as RawPhoto }
        }

        val albumsJob = async(Dispatchers.IO) {
            val _albums = intent.getParcelableArrayExtra("albums") ?: arrayOf()
            CollectionToTreeMap.getAlbumTree(_albums.mapNotNull { it as RawAlbum })
        }

        launch {

            val photos = photosJob.await()
            val albums = albumsJob.await()
            this@ListItemsViewModel.photos.value = photos

            val listItemsJob = getListItemsAsync(photos, albums)
            val list = listItemsJob.await()
            listItems.value = list
        }
    }

    private fun getListItemsAsync(photos: List<RawPhoto>, albums: Map<Int, RawAlbum>): Deferred<List<ListItem>> {
        return async(Dispatchers.IO) {
            ListItemGenerator.generateFromPhotosAndAlbums(photos, albums)
        }
    }
}