package dog.snow.androidrecruittest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dog.snow.androidrecruittest.repository.CollectionToTreeMap
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import java.util.*

class MainActivity : AppCompatActivity(R.layout.main_activity){

    private var users: TreeMap<Int, RawUser>? = null
    private var photos: List<RawPhoto>? = null
    private var albums: TreeMap<Int, RawAlbum>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))

        val _photos = intent.getParcelableArrayExtra("photos") ?: arrayOf()
        val _albums = intent.getParcelableArrayExtra("albums") ?: arrayOf()
        val _users = intent.getParcelableArrayExtra("users") ?: arrayOf()

        photos = _photos.mapNotNull { it as RawPhoto }
        users = CollectionToTreeMap.getUsersTree(_users.mapNotNull { it as RawUser })
        albums = CollectionToTreeMap.getAlbumTree(_albums.mapNotNull { it as RawAlbum })

    }
}