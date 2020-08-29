package dog.snow.androidrecruittest.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.ui.adapter.ListAdapter
import dog.snow.androidrecruittest.ui.model.ListItem
import dog.snow.androidrecruittest.viewmodels.AlbumsViewModel
import dog.snow.androidrecruittest.viewmodels.ListItemsViewModel
import dog.snow.androidrecruittest.viewmodels.PhotosViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : Fragment(R.layout.list_fragment) {

    private val listItemsViewModel: ListItemsViewModel by viewModel()
    private val albumsViewModel: AlbumsViewModel by sharedViewModel()
    private val photosViewModel: PhotosViewModel by sharedViewModel()

    private val adapter = ListAdapter {i, p, v -> onClickItemList(i, p, v)}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewGroup = this.view
        if(viewGroup is ViewGroup) {
            val recyclerView = viewGroup.findViewById<RecyclerView>(R.id.rv_items)
            recyclerView.adapter = adapter
        }

        listItemsViewModel.listItems.observe(this.viewLifecycleOwner, Observer { listItems -> updateItemListAdapter(listItems) })

        albumsViewModel.albums.observe(this.viewLifecycleOwner, Observer { albums ->
            photosViewModel.photos.value?.let {
                updateItemsListViewModel(albums, it)
            }
        })

        photosViewModel.photos.observe(this.viewLifecycleOwner, Observer { photos ->
            albumsViewModel.albums.value?.let {
                updateItemsListViewModel(it, photos)
            }
        })

        tryUpdatingBefore()
    }

    /**
     * To try to update the list based on view models without
     * waiting for observers to fire, in case the data is ready
     * already.
     */
    private fun tryUpdatingBefore() {
        listItemsViewModel.listItems.value?.let {
            updateItemListAdapter(it)
            return
        }
        albumsViewModel.albums.value?.let {albums ->
            photosViewModel.photos.value?.let {photos ->
                updateItemsListViewModel(albums, photos)
            }
        }
    }

    private fun updateItemsListViewModel(albums: Map<Int, RawAlbum>, photos: List<RawPhoto>) {
        listItemsViewModel.prepareListItems(photos, albums)
    }

    private fun updateItemListAdapter(list: List<ListItem>) {
        val viewGroup = this.view
        if(viewGroup is ViewGroup) {
            val recyclerView = viewGroup.findViewById<RecyclerView>(R.id.rv_items)
            (recyclerView.adapter as ListAdapter).submitList(list)
            (recyclerView.adapter as ListAdapter).notifyDataSetChanged()
        }
    }

    private fun onClickItemList(i: ListItem, pos: Int, v: View) {
        Toast.makeText(this.context, "Clicked ${i.title}", Toast.LENGTH_SHORT).show()
    }
}