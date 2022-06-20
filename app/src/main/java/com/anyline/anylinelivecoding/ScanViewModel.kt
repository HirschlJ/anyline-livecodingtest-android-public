package com.anyline.anylinelivecoding

import android.net.wifi.ScanResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import io.anyline.plugin.barcode.BarcodeScanResult
import io.anyline.plugin.barcode.ScannedBarcodesListener
import timber.log.Timber

class ScanViewModel: ViewModel(), ScannedBarcodesListener {

    private val _scanResult = MutableLiveData<String>()
    val scanResult: LiveData<String> = _scanResult

    override fun scannedBarcodes(scanResults: BarcodeScanResult?) {
        scanResults?.result?.let { result ->
            if(result.size > 0) {
                Timber.d("Received ${result.size} results")
                result.forEachIndexed { i, barcode ->
                    Timber.d("Result $i: ${barcode.value}, format = ${barcode.barcodeFormat}")
                }
                if(result.size > 1) {
                    Timber.e("Single result expected but received ${result.size}! Only first result will be displayed.")
                }
                _scanResult.value = result[0].value
            } else {
                Timber.e("BarcodeScanResult contained no results!")
            }

        }
    }
}