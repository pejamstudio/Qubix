package com.example.whitecube.PengaturanDevice

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.whitecube.MainActivity
import com.example.whitecube.Model.DeviceModel
import com.example.whitecube.Model.UserModel
import com.example.whitecube.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_pengaturan_device.*
import kotlinx.android.synthetic.main.activity_pengaturan_device.btn_back
import kotlinx.android.synthetic.main.activity_register_user.*

class PengaturanDevice : AppCompatActivity() {

    private lateinit var refdevice : DatabaseReference
    private lateinit var SP : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan_device)
        SP = getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        refdevice = FirebaseDatabase.getInstance().getReference("device")
        setDeviceData()
        val modeUser = SP.getString("modeUser","").toString()

        if (modeUser == "1"){
            editnamadevice2.visibility = View.GONE

        }else{
            labelHeader.text = "Tentang Device"
            editnamadevice.visibility = View.GONE
            editLokasi.visibility = View.GONE
            berbagiDevice.visibility = View.GONE
        }
        editnamadevice.setOnClickListener {
            startActivity(Intent(this,EditNamaDevice::class.java))
        }
        editLokasi.setOnClickListener {
            startActivity(Intent(this,EditLokasiDevice::class.java))
        }
        btn_back.setOnClickListener{
            this.finish()
        }
        berbagiDevice.setOnClickListener {
            startActivity(Intent(this,BerbagiDevice::class.java))
        }
    }



    override fun onResume() {
        super.onResume()
        setDeviceData()
    }
    private fun setDeviceData(){
        namaDevice.text = SP.getString("namaDevice","")
        namaDevice2.text = SP.getString("namaDevice","")
        idDevice.text = SP.getString("idDevice","")
    }

}
