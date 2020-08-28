package dog.snow.androidrecruittest.repository

import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto

object SetGenerator {
    fun getAlbumIdsFromPhotos(list: List<RawPhoto>): Set<Int> =
        list.map { photo -> photo.albumId }.toSet()

    fun getUserIdsFromPhotos(list: List<RawAlbum>): Set<Int> =
        list.map { album -> album.userId }.toSet()
}