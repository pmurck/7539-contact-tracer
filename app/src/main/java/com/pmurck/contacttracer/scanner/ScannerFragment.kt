package com.pmurck.contacttracer.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.pmurck.contacttracer.R

class ScannerFragment : Fragment() {

    companion object {
        fun newInstance() = ScannerFragment()
        private const val REQUEST_CAMERA_PERMISSION = 10
    }

    private lateinit var textureView: PreviewView

    private lateinit var viewModel: ScannerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val viewgroup = inflater.inflate(R.layout.fragment_scanner, container, false)

        textureView = viewgroup.findViewById(R.id.texture_view)

        // Request camera permissions
        if (isCameraPermissionGranted()) {
            textureView.post { startCamera() }
        } else {
            this.requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }

        return viewgroup

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScannerViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun startCamera() {
        val executor = ContextCompat.getMainExecutor(this.requireActivity().applicationContext)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireActivity().applicationContext)
        cameraProviderFuture.addListener(Runnable {
            val cameraSelector = CameraSelector.Builder()
                // We want to show input from back camera of the device
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(textureView.surfaceProvider) }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val qrCodeAnalyzer = QrCodeAnalyzer { qrCodes ->
                qrCodes.forEach {
                    Log.d("MainActivity", "QR Code detected: ${it.rawValue}")
                    Toast.makeText(this.requireActivity(), "QR Code detected: ${it.rawValue}", Toast.LENGTH_LONG)
                }
            }

            imageAnalysis.setAnalyzer(executor, qrCodeAnalyzer)

            // We need to bind preview and imageAnalysis use cases
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview, imageAnalysis)
        }, executor)
    }

    private fun isCameraPermissionGranted(): Boolean {
        val selfPermission =
            ContextCompat.checkSelfPermission(this.requireActivity().baseContext, Manifest.permission.CAMERA)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (isCameraPermissionGranted()) {
                textureView.post { startCamera() }
            } else {
                Toast.makeText(this.requireActivity(), "Camera permission is required.", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

}