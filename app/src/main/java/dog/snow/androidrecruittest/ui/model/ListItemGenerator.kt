package dog.snow.androidrecruittest.ui.model

import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import java.util.*

object ListItemGenerator {
    fun generateFromPhotosAndAlbums(photos:List<RawPhoto>, albums: Map<Int, RawAlbum>):List<ListItem> =
        photos.map { p -> ListItem(p.id, p.title, albums[p.albumId]?.title ?: "", p.thumbnailUrl) }
}