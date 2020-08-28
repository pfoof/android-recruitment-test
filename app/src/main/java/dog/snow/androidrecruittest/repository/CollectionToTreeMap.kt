package dog.snow.androidrecruittest.repository

import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawUser
import java.util.*

object CollectionToTreeMap {

    fun getAlbumTree(albums: List<RawAlbum>): TreeMap<Int, RawAlbum> {
        val treeMap = TreeMap<Int, RawAlbum>()
        albums.forEach {
            treeMap[it.id] = it
        }
        return treeMap
    }

    fun getUsersTree(users:List<RawUser>): TreeMap<Int, RawUser> {
        val treeMap = TreeMap<Int, RawUser>()
        users.forEach {
            treeMap[it.id] = it
        }
        return treeMap
    }
}