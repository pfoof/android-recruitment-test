package dog.snow.androidrecruittest.repository.service

import dog.snow.androidrecruittest.repository.model.RawPhoto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoService {

    @GET("/photos")
    fun getAllPhotos(): Call<List<RawPhoto>>

    @GET("/photos")
    fun getPhotos(@Query("_limit") limit: Int): Call<List<RawPhoto>>


}