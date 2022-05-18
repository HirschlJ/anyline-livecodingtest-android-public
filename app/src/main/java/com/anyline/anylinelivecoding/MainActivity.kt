package com.anyline.anylinelivecoding

import android.os.Bundle
import com.anyline.anylinelivecoding.databinding.ActivityMainBinding
import timber.log.Timber

private lateinit var binding: ActivityMainBinding

class MainActivity : CameraPermissionActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.requestCameraPermissionButton.setOnClickListener {
            executeIfCameraPermissionGranted {
                Timber.d("--- Camera permission has been granted!")

                /* TODO implement whatever we want to do with granted Camera Permission*/

            }
        }
    }
}