package dog.snow.androidrecruittest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(R.layout.main_activity){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))

        val photos = intent.getParcelableArrayExtra("photos")
        val albums = intent.getParcelableArrayExtra("albums")
        val users = intent.getParcelableArrayExtra("users")
    }
}