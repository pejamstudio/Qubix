package com.example.whitecube.GantiDevice

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whitecube.MainActivity
import com.example.whitecube.Model.DeviceModel
import com.example.whitecube.Model.ModeUserModel
import com.example.whitecube.Model.UserModel
import com.example.whitecube.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_ganti_device.*

class GantiDevice : AppCompatActivity(),OnItemClickListener {

    val devices = mutableListOf<PilihDevice>()
    private lateinit var mAdapter: RecyclerAdapter
    private lateinit var SP : SharedPreferences
    private lateinit var refdevice : DatabaseReference
    private lateinit var refmode : DatabaseReference



    override fun onItemClicked(device: PilihDevice) {
        changeDevice(device.id)
        val backgrond = object : Thread(){
            override fun run() {
                try {
                    java.lang.Thread.sleep(1000)
                    //tambah loading
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                    finish()
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        backgrond.start()

    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(applicationContext,MainActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganti_device)
        refdevice = FirebaseDatabase.getInstance().getReference("device")
        refmode = FirebaseDatabase.getInstance().getReference("modeuser")
        SP = getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)

        readData(object : FirebaseCallback{
            override fun onCallback(list: MutableList<PilihDevice>) {
                addData(devices)
            }
        })
    }

    private fun addData(list: MutableList<PilihDevice>){
        rv.layoutManager = LinearLayoutManager(this)
        mAdapter = RecyclerAdapter(list, this)
        rv.adapter = mAdapter
    }

    private fun readData(firebaseCallback: FirebaseCallback){
        refdevice.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(akun: DataSnapshot) {
                if(akun.exists()){
                    for(c in  akun.children){
                        val data = c.getValue(DeviceModel::class.java)
                        if(data!!.user.contains(SP.getString("id","").toString())){
                            if(data.id == SP.getString("idDevice","").toString()){
                                devices.add(PilihDevice(data.id,data.nama,"1"))
                            }else{
                                devices.add(PilihDevice(data.id,data.nama,"0"))
                            }
                        }
                    }
                    firebaseCallback.onCallback(devices)

                }
            }
        })
    }

    private interface  FirebaseCallback{
        fun onCallback(list : MutableList<PilihDevice>)
    }

    private fun changeDevice(id: String){
        val query = refdevice.orderByChild("id").equalTo(id)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(device: DataSnapshot) {
                if(device.exists()){
                    for(c in  device.children){
                        val data = c.getValue(DeviceModel::class.java)
                        val editor = SP.edit()
                        editor.putString("idDevice",id)
                        editor.putString("namaDevice", data!!.nama)
                        editor.putString("longitude", data.longitude)
                        editor.putString("atitude", data.atitude)
                        editor.putString("readMode", data.readmode)
                        editor.putString("read", data.read)
                        editor.putString("write", data.write)
                        editor.putString("userDevice", data.user)
                        editor.apply()
                        getModeUser(id)
                    }

                }
            }
        })
    }
    private fun clearSP(){
        val editor = SP.edit()
        editor.remove("idDevice")
        editor.remove("namaDevice")
        editor.remove("longitude")
        editor.remove("atitude")
        editor.remove("readMode")
        editor.remove("read")
        editor.remove("write")
        editor.remove("userDevice")
        editor.apply()
    }

    private fun getModeUser (idDevice: String) {
        val query = refmode.orderByChild("iddevice").equalTo(idDevice)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(device: DataSnapshot) {
                if(device.exists()){
                    for(c in  device.children){
                        val data = c.getValue(ModeUserModel::class.java)
                        val editor = SP.edit()
                        editor.putString("modeUser",data!!.mode)
                        editor.apply()
                    }

                }
            }
        })
    }


}
