package dog.snow.androidrecruittest.viewmodels

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import dog.snow.androidrecruittest.repository.CollectionToTreeMap
import dog.snow.androidrecruittest.repository.model.RawUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UsersViewModel: CoroutineViewModel() {

    private var users: MutableLiveData<Map<Int, RawUser>> = MutableLiveData()

    fun prepareUsers(intent: Intent) {
        val usersJob = async(Dispatchers.IO) {
            val _users = intent.getParcelableArrayExtra("users") ?: arrayOf()
            CollectionToTreeMap.getUsersTree(_users.mapNotNull { it as RawUser })
        }
        launch {
            users.value = usersJob.await()
        }
    }

}