package dog.snow.androidrecruittest.repository

import android.content.Context
import dog.snow.androidrecruittest.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Endpoint(val context: Context) {
    val retrofit = Retrofit.Builder()
        .baseUrl(context.resources.getString(R.string.base_url))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}