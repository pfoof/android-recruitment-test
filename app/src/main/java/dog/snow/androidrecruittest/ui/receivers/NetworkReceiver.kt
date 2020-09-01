package dog.snow.androidrecruittest.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import dog.snow.androidrecruittest.MainActivity
import dog.snow.androidrecruittest.extensions.hasNetwork

class NetworkReceiver(): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            if(it is MainActivity)
                it.networkChanged(it.hasNetwork)
        }
    }
}