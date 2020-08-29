package dog.snow.androidrecruittest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dog.snow.androidrecruittest.repository.CollectionToTreeMap
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import dog.snow.androidrecruittest.ui.model.ListItem
import dog.snow.androidrecruittest.viewmodels.AlbumsViewModel
import dog.snow.androidrecruittest.viewmodels.ListItemsViewModel
import dog.snow.androidrecruittest.viewmodels.PhotosViewModel
import dog.snow.androidrecruittest.viewmodels.UsersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity(R.layout.main_activity){

    private val albumsViewModel: AlbumsViewModel by viewModel()
    private val photosViewModel: PhotosViewModel by viewModel()
    private val usersViewModel: UsersViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Most optimistic order
        usersViewModel.prepareUsers(intent)
        albumsViewModel.prepareAlbums(intent)
        photosViewModel.preparePhotos(intent)
    }

}