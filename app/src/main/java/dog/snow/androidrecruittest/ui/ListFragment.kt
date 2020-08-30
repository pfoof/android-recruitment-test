package dog.snow.androidrecruittest.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import dog.snow.androidrecruittest.MainActivity
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.extensions.filterPrefix
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.ui.adapter.ListAdapter
import dog.snow.androidrecruittest.ui.model.ListItem
import dog.snow.androidrecruittest.viewmodels.AlbumsViewModel
import dog.snow.androidrecruittest.viewmodels.ListItemsViewModel
import dog.snow.androidrecruittest.viewmodels.PhotosViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : Fragment(R.layout.list_fragment), TextWatcher {

    private val listItemsViewModel: ListItemsViewModel by viewModel()
    private val albumsViewModel: AlbumsViewModel by sharedViewModel()
    private val photosViewModel: PhotosViewModel by sharedViewModel()

    private var adapter = ListAdapter {i, p, v -> onClickItemList(i, p, v)}
    private var recyclerView: RecyclerView? = null

    private val searchEditText: EditText by lazy { requireView().findViewById<EditText>(R.id.et_search) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_items)
        recyclerView?.adapter = adapter
        searchEditText.addTextChangedListener(this)

        listItemsViewModel.listItems.observe(this.viewLifecycleOwner, Observer {
                listItems -> updateItemListAdapter(listItems)
        })

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

    private fun updateItemListAdapter(inList: List<ListItem>) {

        val list = when(searchEditText.text.isNotBlank() && searchEditText.text.isNotEmpty()) {
            true -> inList.filterPrefix(searchEditText.text.toString())
            false -> inList
        }

        adapter.submitList(list) {
            recyclerView?.visibility = View.VISIBLE

            view?.findViewById<TextView>(R.id.tv_empty)
                ?.visibility = when(list.isEmpty()) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }

            adapter.notifyDataSetChanged()
        }
    }

    private fun onClickItemList(i: ListItem, pos: Int, v: View) {
        try {
            val photo = photosViewModel.photos.value?.get(pos)
            photo?.let {
                (activity as MainActivity)
                    .showDetailsFragment(it)
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            Log.e("ListFragment", "AIOOBE: This should never happen!")
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        listItemsViewModel.listItems.value?.let {
            updateItemListAdapter(it)
        }
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
}