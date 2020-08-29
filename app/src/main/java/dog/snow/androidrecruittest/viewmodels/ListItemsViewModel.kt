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

    fun prepareListItems(photos: List<RawPhoto>, albums: Map<Int, RawAlbum>) {
        val job = async(Dispatchers.IO) {
            ListItemGenerator.generateFromPhotosAndAlbums(photos, albums)
        }
        launch {
            listItems.value = job.await()
        }
    }
}