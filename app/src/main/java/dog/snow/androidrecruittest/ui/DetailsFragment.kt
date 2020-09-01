package dog.snow.androidrecruittest.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import dog.snow.androidrecruittest.MainActivity
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.extensions.loadMocked
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import dog.snow.androidrecruittest.viewmodels.AlbumsViewModel
import dog.snow.androidrecruittest.viewmodels.UsersViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment(private var photo: RawPhoto) : Fragment(R.layout.details_fragment) {

    constructor(): this(RawPhoto(PHOTO_INEXISTENT,0, "", "", "")) {
        // Put dummy photo at first
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.details_fragment_exit)
        enterTransition = inflater.inflateTransition(R.transition.details_fragment_enter)

        /* Restore photo from state
        * and set the fragment to current */
        if(photo.id == PHOTO_INEXISTENT)
            savedInstanceState?.let {
                if(it.containsKey("photo"))
                    it.getParcelable<RawPhoto>("photo")?.let {restored ->
                        photo = restored
                        // Bind this fragment for back button event handling
                        (activity as MainActivity).let { activity ->
                            activity.setCurrentDetailsFragment(this)
                        }
                    }
            }

        /* If photo was not restored
        * remove the fragment */
        if(photo.id == PHOTO_INEXISTENT) {
            activity?.let {
                it.supportFragmentManager
                    .beginTransaction()
                    .remove(this)
                    .commit()
            }
        }

    }

    private val usersViewModel: UsersViewModel by sharedViewModel()
    private val albumsViewModel: AlbumsViewModel by sharedViewModel()

    private var album: RawAlbum? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener {  } // The ignore listener

        usersViewModel.users.observe(this.viewLifecycleOwner, Observer {users ->
            updateUserWithAlbum(users)
        })

        albumsViewModel.albums.observe(this.viewLifecycleOwner, Observer { albums ->
            updateAlbumWithPhoto(albums)
        })

        loadFullSizeImage()
        tryUpdatingBefore()
    }

    /**
     * To try to update the preview based on view models without
     * waiting for observers to fire, in case the data is ready
     * already.
     */
    private fun tryUpdatingBefore() {
        albumsViewModel.albums.value?.let {
            updateAlbumWithPhoto(it)
        }
    }

    private fun updateAlbumWithPhoto(albums: Map<Int, RawAlbum>) {
        if(albums.containsKey(photo.albumId)) {
            albums[photo.albumId]?.let {

                updateAlbumInfo(it)
                this.album = it // Save for the future

                // Trigger this also because now we know which user the photo belongs to
                usersViewModel.users.value?.let {
                    updateUserWithAlbum(it)
                }
            }
        } else
            updateAlbumUnknownInfo()
    }

    private fun updateUserWithAlbum(users: Map<Int, RawUser>) {
        album?.let { album ->
            if(users.containsKey(album.userId)) {
                users[album.userId]?.let {
                    updateUserInfo(it)
                }
            } else
                updateUserUnknownInfo()
        }
    }

    private fun updateAlbumInfo(album: RawAlbum) {
        val v = this.view
        if(v is ViewGroup) {
            val albumTitle = v.findViewById<TextView>(R.id.tv_album_title)
            albumTitle.text = album.title
        }
    }

    private fun updateAlbumUnknownInfo() {
        val v = this.view
        if(v is ViewGroup) {
            val albumTitle = v.findViewById<TextView>(R.id.tv_album_title)
            albumTitle.setText(R.string.unknown)
        }
    }

    private fun updateUserInfo(user: RawUser) {
        val v = this.view
        if(v is ViewGroup) {
            val username = v.findViewById<TextView>(R.id.tv_username)
            val email = v.findViewById<TextView>(R.id.tv_email)
            val phone = v.findViewById<TextView>(R.id.tv_phone)

            username.text = user.username
            email.text = user.email
            phone.text = user.phone
        }
    }

    private fun updateUserUnknownInfo() {
        val v = this.view
        if(v is ViewGroup) {

            val username = v.findViewById<TextView>(R.id.tv_username)
            val email = v.findViewById<TextView>(R.id.tv_email)
            val phone = v.findViewById<TextView>(R.id.tv_phone)

            username.setText(R.string.unknown)
            email.setText(R.string.unknown)
            phone.setText(R.string.unknown)
        }
    }

    private fun loadFullSizeImage() {
        val v = this.view
        if(v is ViewGroup) {
            val imageView = v.findViewById<ImageView>(R.id.iv_photo)
            imageView?.let {
                Glide.with(this)
                    .loadMocked(photo.url)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(imageView)
            }
        }
    }



    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("photo", photo)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val PHOTO_INEXISTENT = -1
    }
}