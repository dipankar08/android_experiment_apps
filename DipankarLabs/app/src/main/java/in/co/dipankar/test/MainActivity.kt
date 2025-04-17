package `in`.co.dipankar.test

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import `in`.co.dipankar.mylibrary.UnityWrapper

class MainActivity : AppCompatActivity() {
    private lateinit var mediaProjectionManager: MediaProjectionManager
    private lateinit var startCaptureBtn: Button
    private lateinit var stopCaptureBtn: Button
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Module text
        UnityWrapper.initialize(this);
        UnityWrapper.setCallback { msg -> Log.d("DIPANKAR", msg) }
        UnityWrapper.startService()
        UnityWrapper.startService()

        // Initialize MediaProjectionManager
        mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        // Initialize buttons
        startCaptureBtn = findViewById(R.id.startCaptureBtn)
        stopCaptureBtn = findViewById(R.id.stopCaptureBtn)

        // Start capture button listener
        startCaptureBtn.setOnClickListener {
            startProjection()
        }

        // Stop capture button listener
        stopCaptureBtn.setOnClickListener {
            stopProjection()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val projectionCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Start the screen capture service and pass the result data
            val serviceIntent = Intent(this, ScreenCaptureService::class.java).apply {
                putExtra("resultCode", result.resultCode)
                putExtra("data", result.data)
            }
            startForegroundService(serviceIntent)
            // Enable Stop button and disable Start button
            stopCaptureBtn.isEnabled = true
            startCaptureBtn.isEnabled = false
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startProjection() {
        val captureIntent = mediaProjectionManager.createScreenCaptureIntent()
        projectionCallback.launch(captureIntent)
    }

    // Stop projection and disable stop button
    private fun stopProjection() {
        val serviceIntent = Intent(this, ScreenCaptureService::class.java)
        stopService(serviceIntent)
        // Enable Start button and disable Stop button
        startCaptureBtn.isEnabled = true
        stopCaptureBtn.isEnabled = false
    }
}
