package com.example.whitecube

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.whitecube.Model.DeviceModel
import com.example.whitecube.Model.ModeUserModel
import com.example.whitecube.Model.UserModel
import com.example.whitecube.PengaturanDevice.PengaturanDevice
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_camera_scan.*
import kotlinx.android.synthetic.main.activity_qrscanner.*
import kotlinx.android.synthetic.main.activity_register_user.*
import java.lang.Exception

class CameraScan : AppCompatActivity() {

    private lateinit var refdevice : DatabaseReference
    private lateinit var refmode : DatabaseReference
    private lateinit var SP : SharedPreferences
    private var firstScan = 1
    private lateinit var  cameraSource:CameraSource
    private lateinit var detector: BarcodeDetector
    private val requestCodeCameraPermission= 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_scan)

        SP = getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        refmode = FirebaseDatabase.getInstance().getReference("modeuser")
        refdevice = FirebaseDatabase.getInstance().getReference("device")

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

    private fun scanDevice(qrcode : String){
        //
        val query = refmode.orderByChild("iddevice").equalTo(qrcode)
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for (u in p0.children){
                        val user = u.getValue(ModeUserModel::class.java)
                        if(user!!.iduser.equals(SP.getString("id",""))){
                            Toast.makeText(applicationContext,"Device sudah terdaftar di akun anda", Toast.LENGTH_LONG).show()
                            finish()
                        }else{
                            cekToAddDevice(qrcode,"0")
                        }
                    }
                }else{
                    cekToAddDevice(qrcode,"1")
                }
            }

        })
    }

    private fun cekToAddDevice(qrcode: String,mode: String){
        val query = refdevice.orderByChild("id").equalTo(qrcode)
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for (u in p0.children){
                        val device = u.getValue(DeviceModel::class.java)
                        if(device!!.id.equals(qrcode)){
                            val editor = SP.edit()
                            editor.putString("idDevice",qrcode)
                            editor.putString("namaDevice", device.nama)
                            editor.putString("longitude", device.longitude)
                            editor.putString("atitude", device.atitude)
                            editor.putString("modeUser", mode)
                            editor.putString("readMode", device.readmode)
                            editor.putString("read", device.read)
                            editor.putString("write", device.write)
                            editor.putString("userDevice", device.user)
                            editor.apply()
                        }
                    }
                    addDevice(qrcode,mode)
                }else{
                    Toast.makeText(applicationContext,"Device tidak terdaftar di Qubix", Toast.LENGTH_LONG).show()
                    finish()
                }
            }

        })
    }

    private fun addDevice(qrcode: String,mode : String){
        val id = SP.getString("idDevice","").toString()
        val nama = SP.getString("namaDevice","").toString()
        val longitude = SP.getString("longitude","").toString()
        val atitude = SP.getString("atitude","").toString()
        val readMode = SP.getString("readMode","").toString()
        val read = SP.getString("read","").toString()
        val write = SP.getString("write","").toString()
        val userDevice = SP.getString("userDevice","").toString()


        val device = DeviceModel(id,nama,longitude,atitude,readMode,read,write,userDevice+" "+SP.getString("id","").toString())

        refdevice.child(id).setValue(device)

        val modeId = refmode.push().key.toString()

        val user = ModeUserModel(modeId,SP.getString("id","").toString(),qrcode,mode)

        refmode.child(modeId).setValue(user).addOnCompleteListener {
            if(mode == "1"){
                finish()
                startActivity(Intent(this,PengaturanDevice::class.java))
            }else{
                finish()
            }
            Toast.makeText(applicationContext,"Device berhasil ditambahkan", Toast.LENGTH_LONG).show()
        }
    }

    private val processor = object : Detector.Processor<Barcode>{
        override fun release() {

        }

        override fun receiveDetections(detection : Detector.Detections<Barcode>?) {
            if(detection != null && detection.detectedItems != null && firstScan == 1){
                val qrCode: SparseArray<Barcode> = detection.detectedItems
                val code = qrCode.valueAt(0)
                firstScan = 0
                scanDevice(code.displayValue)
            }
        }
    }
}
