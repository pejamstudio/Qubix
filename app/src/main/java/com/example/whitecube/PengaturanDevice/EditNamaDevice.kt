package com.example.whitecube.PengaturanDevice

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whitecube.Model.DeviceModel
import com.example.whitecube.Model.UserModel
import com.example.whitecube.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register_user.*

class EditNamaDevice : AppCompatActivity() {

    private lateinit var refdevice : DatabaseReference
    private lateinit var SP : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        refdevice = FirebaseDatabase.getInstance().getReference("device")
        SP = getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)

        init()
        btn_save.setOnClickListener {
            updateNamaDevice()
        }
        btn_back.setOnClickListener {
            this.finish()
        }
    }
    private fun init(){
        namaUser.setText(SP.getString("namaDevice",""))
        headerNama.text = "Atur Nama Device"
        labelNama.text = "Nama Device"
        nb.text = "Contoh : Rumah Blok R-3, Kantor Candi, dll"
    }

    private fun updateNamaDevice(){
        val id = SP.getString("idDevice","").toString()
        val readMode = SP.getString("readMode","").toString()
        val read = SP.getString("read","").toString()
        val write = SP.getString("write","").toString()
        val longitude = SP.getString("longitude","").toString()
        val atitude = SP.getString("atitude","").toString()
        val userDevice = SP.getString("userDevice","").toString()

        val user = DeviceModel(id,namaUser.text.toString(),longitude,atitude,readMode,read,write,userDevice)
        val editor = SP.edit()
        editor.putString("namaDevice",namaUser.text.toString())
        editor.apply()
        refdevice.child(SP.getString("idDevice","").toString()).setValue(user).addOnCompleteListener {
            this.finish()
        }
    }
}
