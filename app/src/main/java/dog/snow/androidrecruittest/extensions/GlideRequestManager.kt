package dog.snow.androidrecruittest.extensions

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

private val lazyHeaders = LazyHeaders.Builder()
    .addHeader("User-Agent", "Mozilla/5.0 Snow Dog User Agent 1.0 (Android)")
    .addHeader("App-Agent", "Mozilla/5.0 Snow Dog App Agent 1.0 (Android)")
    .build()

fun RequestManager.loadMocked(url: String): RequestBuilder<Drawable> =
    this.load(GlideUrl(url, lazyHeaders))