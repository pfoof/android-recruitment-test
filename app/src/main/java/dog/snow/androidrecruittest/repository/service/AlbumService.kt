package dog.snow.androidrecruittest.repository.service

import dog.snow.androidrecruittest.repository.model.RawAlbum
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumService {

    @GET("/albums/{id}")
    fun getAlbum(@Path("id") id: Int): Call<RawAlbum>;

}