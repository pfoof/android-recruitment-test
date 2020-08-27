package dog.snow.androidrecruittest.repository

import android.content.Context
import dog.snow.androidrecruittest.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Endpoint(context: Context) {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(context.resources.getString(R.string.base_url))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}