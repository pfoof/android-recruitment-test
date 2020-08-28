package dog.snow.androidrecruittest.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*

val Context.hasNetwork: Boolean
    get() {
        val manager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = manager.activeNetwork ?: return false
            val caps = manager.getNetworkCapabilities(network) ?: return false
            return caps.hasTransport(TRANSPORT_CELLULAR) || caps.hasTransport(TRANSPORT_WIFI) || caps.hasTransport(TRANSPORT_ETHERNET)
        } else {
            val network = manager.activeNetworkInfo ?: return false
            return network.type == TYPE_WIFI || network.type == TYPE_MOBILE || network.type == TYPE_ETHERNET
        }
    }