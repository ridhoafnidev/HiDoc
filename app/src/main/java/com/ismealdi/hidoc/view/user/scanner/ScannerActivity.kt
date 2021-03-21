package com.ismealdi.hidoc.view.user.scanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmActivity
import com.ismealdi.hidoc.utils.commons.Constants
import com.ismealdi.hidoc.utils.commons.Constants.INTENT.ACTIVITY.BARCODE
import com.ismealdi.hidoc.utils.components.barcode.BarcodeGraphicTracker
import com.ismealdi.hidoc.utils.components.barcode.BarcodeTrackerFactory
import kotlinx.android.synthetic.main.activity_scanner.*

class ScannerActivity : AmActivity(R.layout.activity_scanner), BarcodeGraphicTracker.BarcodeUpdateListener {

    private var cameraSource: CameraSource? = null
    private var builder: CameraSource.Builder? = null

    override fun initView() {
        initCameraSource()
    }

    private fun initCameraSource() {
        graphicOverlay?.let { overlay ->

            val detector = BarcodeDetector.Builder(this).build()
            val factory = BarcodeTrackerFactory(overlay, this)

            detector.setProcessor(MultiProcessor.Builder(factory).build())

            builder = CameraSource.Builder(this, detector).setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedFps(5.0f)

            builder?.let { build ->
                cameraSource = build.build()
            }

        }
    }

    override fun initListener() {
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun checkCamera() {
        val permissionCheckStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permissionCheckStorage != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), Constants.INTENT.REQUEST.PERMISSION)
        } else {
            cameraSource?.let { source ->
                graphicOverlay?.let { overlay ->
                    preview?.start(source, overlay)
                } ?: run {
                    source.release()
                    cameraSource = null
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == Constants.INTENT.REQUEST.PERMISSION) {
            checkCamera()
        }
    }

    override fun onResume() {
        super.onResume()
        checkCamera()
    }

    override fun onPause() {
        super.onPause()
        preview?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        preview?.release()
    }

    override fun onBarcodeDetected(barcode: Barcode?) {
        barcode?.let { code ->
            val data = Intent()

            data.putExtra(BARCODE, code)
            setResult(CommonStatusCodes.SUCCESS, data)

            textBarcode.text = code.displayValue
        }
    }


}