package dog.snow.androidrecruittest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dog.snow.androidrecruittest.viewmodels.AlbumsViewModel
import dog.snow.androidrecruittest.viewmodels.PhotosViewModel
import dog.snow.androidrecruittest.viewmodels.UsersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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