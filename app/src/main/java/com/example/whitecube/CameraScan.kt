package com.example.whitecube

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isEmpty
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_camera_scan.*
import kotlinx.android.synthetic.main.activity_qrscanner.*
import java.lang.Exception

class CameraScan : AppCompatActivity() {

    private var firstScan = 1
    private lateinit var  cameraSource:CameraSource
    private lateinit var detector: BarcodeDetector
    private val requestCodeCameraPermission= 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_scan)
        if(ContextCompat.checkSelfPermission(this@CameraScan,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            cameraPermission()
        }else{
            setupControl()
        }
    }

    private fun setupControl(){
        detector = BarcodeDetector.Builder(this@CameraScan).build()
        cameraSource = CameraSource.Builder(this@CameraScan,detector)
            .setAutoFocusEnabled(true)
            .build()
        cameraSurfaceView.holder.addCallback(surfaceCallBack)
        detector.setProcessor(processor)
    }
    private fun cameraPermission(){
        ActivityCompat.requestPermissions(
            this@CameraScan, arrayOf(Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setupControl()
            }else{
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    }
    private val surfaceCallBack = object : SurfaceHolder.Callback{
        override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

        }

        override fun surfaceDestroyed(p0: SurfaceHolder?) {
            cameraSource.stop()
        }

        override fun surfaceCreated(surfaceHolder: SurfaceHolder?) {
            try {
                cameraSource.start(surfaceHolder)
            }catch (exception:Exception){
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun gotoQRActivity(qrcode : String){


        val intent = Intent(this,QRscanner::class.java)
        intent.putExtra("activity","CamScan")
        intent.putExtra("qrcode",qrcode)
        startActivity(intent)


    }

    private val processor = object : Detector.Processor<Barcode>{
        override fun release() {

        }

        override fun receiveDetections(detection : Detector.Detections<Barcode>?) {
            if(detection != null && detection.detectedItems != null && firstScan == 1){
                val qrCode: SparseArray<Barcode> = detection.detectedItems
                val code = qrCode.valueAt(0)
                firstScan = 0
                gotoQRActivity(code.displayValue)
            }
        }
    }
}
