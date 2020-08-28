package dog.snow.androidrecruittest.extensions

import dog.snow.androidrecruittest.SplashActivity
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

fun SplashActivity.getPhotosAsync(): Deferred<List<RawPhoto>?> =
    coroutineScope.async(Dispatchers.IO) {
        return@async photoService.getPhotos(100).execute().body()
    }

fun SplashActivity.getUsersAsync(users: Set<Int>): Deferred<Set<RawUser?>> =
    coroutineScope.async(Dispatchers.IO) {
        return@async users.map { u -> getUserAsync(u) }.toSet()
    }

fun SplashActivity.getUserAsync(id: Int): RawUser?
        = userService.getUser(id).execute().body()

fun SplashActivity.getAlbumsAsync(albums: Set<Int>): Deferred<Set<RawAlbum?>> =
    coroutineScope.async(Dispatchers.IO) {
        return@async albums.map { a -> getAlbumAsync(a) }.toSet()
    }

fun SplashActivity.getAlbumAsync(id: Int): RawAlbum?
        = albumService.getAlbum(id).execute().body()