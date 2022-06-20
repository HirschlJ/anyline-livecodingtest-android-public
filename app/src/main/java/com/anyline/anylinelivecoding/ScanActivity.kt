package com.anyline.anylinelivecoding

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.anyline.anylinelivecoding.databinding.ActivityScanBinding
import io.anyline.AnylineSDK
import io.anyline.camera.CameraController
import io.anyline.camera.CameraOpenListener
import io.anyline.plugin.barcode.BarcodeFormat
import io.anyline.plugin.barcode.BarcodeScanViewPlugin
import timber.log.Timber
import java.lang.Exception

class ScanActivity : AppCompatActivity(), CameraOpenListener {

    private lateinit var binding: ActivityScanBinding

    private lateinit var barcodeScanViewPlugin: BarcodeScanViewPlugin

    private val viewModel: ScanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AnylineSDK.init(getString(R.string.anyline_license_key), this)

        //configuration variant 1 (as shown in general example)
        /*val scanViewPluginConfig = ScanViewPluginConfig(applicationContext, "barcode_view_config.json")
        barcodeScanViewPlugin = BarcodeScanViewPlugin(applicationContext, scanViewPluginConfig, "BARCODE")
        val scanViewConfig = BaseScanViewConfig(applicationContext, "barcode_view_config.json")


        binding.scanView.apply {
            setScanViewConfig(scanViewConfig)
            scanViewPlugin = barcodeScanViewPlugin
            setCameraOpenListener(this@ScanActivity)
        }*/

        //Configuration variant 2 (shown in view configuration)
        with(binding.scanView) {
            init("barcode_view_config.json")
            barcodeScanViewPlugin = BarcodeScanViewPlugin(applicationContext, scanViewPluginConfig, "BARCODE")
            scanViewPlugin = barcodeScanViewPlugin
            setCameraOpenListener(this@ScanActivity)
        }

        //Necessary??
        barcodeScanViewPlugin.setBarcodeFormats(BarcodeFormat.QR_CODE, BarcodeFormat.DATA_MATRIX)

        barcodeScanViewPlugin.addScannedBarcodesListener(viewModel)

        viewModel.scanResult.observe(this) { result ->
            Timber.d("Received result $result")
            binding.cvResult.isVisible = result.isNotEmpty()
            binding.tvResult.text = getString(R.string.tv_scan_result, result)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.scanView.start()
    }

    override fun onPause() {
        super.onPause()
        binding.scanView.stop()
        binding.scanView.releaseCameraInBackground()
    }

    override fun onCameraOpened(controller: CameraController?, width: Int, heigth: Int) {
        Timber.d("Camera opened successfully. Frame resolution $width x $heigth")
    }

    override fun onCameraError(e: Exception?) {
        throw RuntimeException(e)
    }

    companion object {
        fun buildIntent(context: Context): Intent {
            return Intent(context, ScanActivity::class.java)
        }
    }
}