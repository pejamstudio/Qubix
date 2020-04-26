package com.example.whitecube.PengaturanDevice

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.whitecube.Model.DeviceModel
import com.example.whitecube.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_lokasi_device.*
import java.util.jar.Manifest

class EditLokasiDevice : AppCompatActivity() {

    val RequestPermissionCode = 1
    var mLocation: Location? = null
    private lateinit var refdevice : DatabaseReference
    private lateinit var SP : SharedPreferences
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_lokasi_device)
        SP = getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        refdevice = FirebaseDatabase.getInstance().getReference("device")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        btn_set.setOnClickListener {
            getLocation()
        }
        btn_back.setOnClickListener {
            finish()
        }

    }

    private fun getLocation(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermission()

        }else{
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {location: Location? ->
                mLocation = location
                if(location!=null){
                    addLocation(location.longitude.toString(),location.latitude.toString())
                }
            }
        }


    }
    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),RequestPermissionCode)
        this.recreate()
    }
    private fun addLocation(longitude: String,atitude: String){
        val id = SP.getString("idDevice","").toString()
        val nama =  SP.getString("namaDevice","").toString()
        val readMode = SP.getString("readMode","").toString()
        val read = SP.getString("read","").toString()
        val write = SP.getString("write","").toString()
        val userDevice = SP.getString("userDevice","").toString()


        val device = DeviceModel(id,nama,longitude,atitude,readMode,read,write,userDevice)

        refdevice.child(id).setValue(device).addOnCompleteListener {
            val editor = SP.edit()
            editor.putString("longitude", longitude)
            editor.putString("atitude", atitude)
            editor.apply()
            finish()
            Toast.makeText(this,"Pengaturan lokasi device behasil",Toast.LENGTH_SHORT).show()
        }
    }
}
