package dog.snow.androidrecruittest

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dog.snow.androidrecruittest.extensions.hasNetwork
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.ui.DetailsFragment
import dog.snow.androidrecruittest.ui.receivers.NetworkReceiver
import dog.snow.androidrecruittest.viewmodels.AlbumsViewModel
import dog.snow.androidrecruittest.viewmodels.PhotosViewModel
import dog.snow.androidrecruittest.viewmodels.UsersViewModel
import kotlinx.android.synthetic.main.layout_banner.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.main_activity) {

    private val albumsViewModel: AlbumsViewModel by viewModel()
    private val photosViewModel: PhotosViewModel by viewModel()
    private val usersViewModel: UsersViewModel by viewModel()

    private val networkReceiver = NetworkReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))

        if(Build.VERSION.SDK_INT >= 28)
            (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .registerDefaultNetworkCallback(networkCallback)
        else
            registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        // Most optimistic order
        usersViewModel.prepareUsers(intent)
        albumsViewModel.prepareAlbums(intent)
        photosViewModel.preparePhotos(intent)

    }

    override fun onDestroy() {
        super.onDestroy()
        if(Build.VERSION.SDK_INT < 28)
            unregisterReceiver(networkReceiver)
    }

    private var detailsFragment: DetailsFragment? = null
    fun showDetailsFragment(photo: RawPhoto) {
        val transaction = supportFragmentManager.beginTransaction()

        detailsFragment?.let {
            transaction.remove(it)
        }

        detailsFragment = DetailsFragment(photo).also {
            transaction
                .add(R.id.coordinator, it)
                .commit()
        }
    }

    fun setCurrentDetailsFragment(fragment: DetailsFragment) {
        detailsFragment = fragment
    }

    override fun onBackPressed() {

        detailsFragment?.let {
            if(supportFragmentManager.fragments.contains(it)) {
                supportFragmentManager
                    .beginTransaction()
                    .remove(it)
                    .commit()
            }
            detailsFragment = null
            return
        }

        super.onBackPressed()

    }

    fun networkChanged(hasNetwork: Boolean) {
        runOnUiThread {
            banner.visibility = when(hasNetwork) {
                false -> View.VISIBLE
                true -> View.GONE
            }
        }
    }

    private val networkCallback = object: ConnectivityManager.NetworkCallback() {
        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            super.onBlockedStatusChanged(network, blocked)
            networkChanged(!blocked)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            networkChanged(this@MainActivity.hasNetwork)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            networkChanged(false)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            networkChanged(false)
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            networkChanged(true)
        }
    }
}