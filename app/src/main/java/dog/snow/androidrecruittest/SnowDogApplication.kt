package dog.snow.androidrecruittest

import android.app.Application
import dog.snow.androidrecruittest.repository.Endpoint
import dog.snow.androidrecruittest.repository.service.AlbumService
import dog.snow.androidrecruittest.repository.service.PhotoService
import dog.snow.androidrecruittest.repository.service.UserService
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

class SnowDogApplication: Application() {

    private val endpointModule = module {
        single { Endpoint(androidContext()) }
        single<PhotoService> { inject<Endpoint>().value.retrofit.create(PhotoService::class.java) }
        single<AlbumService> { inject<Endpoint>().value.retrofit.create(AlbumService::class.java) }
        single<UserService> { inject<Endpoint>().value.retrofit.create(UserService::class.java) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin(
            this@SnowDogApplication,
            listOf(endpointModule)
        )
    }
}