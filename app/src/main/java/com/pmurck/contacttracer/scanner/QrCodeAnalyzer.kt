package com.pmurck.contacttracer.scanner

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage


class QrCodeAnalyzer(
    private val onQrCodesDetected: (qrCodes: List<Barcode>) -> Unit
) : ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        //val rotation = rotationDegreesToFirebaseRotation(rotationDegrees)
        val visionImage = InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        val detector = BarcodeScanning.getClient(options)

        detector.process(visionImage)
            .addOnSuccessListener { barcodes ->
                onQrCodesDetected(barcodes)
            }
            .addOnFailureListener {
                Log.e("QrCodeAnalyzer", "something went wrong", it)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }

    }

}
