
package `in`.co.dipankar.mylibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import java.sql.Blob

object UnityWrapper {

    private lateinit var context: Context
    private var callback: ((String) -> Unit)? = null

    fun initialize(context: Context) {
        this.context = context
        Log.d("UnityWrapper", "Initialized with context")
    }

    fun setCallback(callback: (String) -> Unit) {
        this.callback = callback
        Log.d("UnityWrapper", "Callback registered")
    }

    fun logMessage(message: String) {
        Log.d("UnityWrapper", message)
        callback?.invoke(message)
    }

    fun startService() {
       // val serviceIntent = Intent(context, ScreenCaptureService::class.java)
        //context.startService(serviceIntent)
        Log.d("UnityWrapper", "Service started")
    }

    fun stopService() {
       // val serviceIntent = Intent(context, ScreenCaptureService::class.java)
        //context.stopService(serviceIntent)
        Log.d("UnityWrapper", "Service stopped")
    }
}


