package dog.snow.androidrecruittest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dog.snow.androidrecruittest.repository.Endpoint
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity(R.layout.splash_activity) {

    private val endpoint:Endpoint by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(endpoint.retrofit != null)
            Toast.makeText(this, "Endpoint created", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "Endpoint NOT created!!!", Toast.LENGTH_SHORT).show()
    }

    private fun showError(errorMessage: String?) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.cant_download_dialog_title)
            .setMessage(getString(R.string.cant_download_dialog_message, errorMessage))
            .setPositiveButton(R.string.cant_download_dialog_btn_positive) { _, _ -> /*tryAgain()*/ }
            .setNegativeButton(R.string.cant_download_dialog_btn_negative) { _, _ -> finish() }
            .create()
            .apply { setCanceledOnTouchOutside(false) }
            .show()
    }
}