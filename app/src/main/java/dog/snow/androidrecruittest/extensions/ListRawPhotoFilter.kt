package dog.snow.androidrecruittest.extensions

import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.ui.model.ListItem

fun List<ListItem>.filterPrefix(string: String): List<ListItem> =
    this.filter { item ->
        item.title.startsWith(string, ignoreCase = true)
                || item.albumTitle.startsWith(string, ignoreCase = true)
                || item.title.contains(" $string", ignoreCase = true)
                || item.albumTitle.contains(" $string", ignoreCase = true)
    }