package dog.snow.androidrecruittest

import android.app.Application
import dog.snow.androidrecruittest.repository.Endpoint
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

class SnowDogApplication: Application() {

    private val endpointModule = module {
        single<Endpoint> { Endpoint(androidContext()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin(
            this@SnowDogApplication,
            listOf(endpointModule)
        )
    }
}