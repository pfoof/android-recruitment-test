package dog.snow.androidrecruittest.viewmodels

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class CoroutineViewModel : ViewModel(), CoroutineScope {
    private val parentJob = Job()

    override val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}