package com.example.whitecube.Relay

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whitecube.Model.DeviceModel
import com.example.whitecube.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register_user.*

class ReadIrRemote : AppCompatActivity() {

    private lateinit var SP : SharedPreferences
    private lateinit var refdevice : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_ir_remote)
        SP = getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        refdevice = FirebaseDatabase.getInstance().getReference("device")
        val query = refdevice.orderByChild("id").equalTo(SP.getString("idDevice","").toString())

        setReadMode("1")
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(akun: DataSnapshot) {
                if(akun.exists()){
                    for(c in  akun.children){
                        val data = c.getValue(DeviceModel::class.java)
                        if(data!!.readmode == "0"){
                            val editor = SP.edit()
                            editor.putString("irKode", data.read)
                            editor.apply()
                            Toast.makeText( applicationContext,"IR kode berhasil diatur",Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setReadMode("0")
        Toast.makeText( this,"IR kode gagal diatur",Toast.LENGTH_SHORT).show()
    }

    private fun setReadMode(readmode : String){
        val id = SP.getString("idDevice","").toString()
        val nama = SP.getString("namaDevice","").toString()
        val read = SP.getString("read","").toString()
        val write = SP.getString("write","").toString()
        val longitude = SP.getString("longitude","").toString()
        val atitude = SP.getString("atitude","").toString()
        val userDevice = SP.getString("userDevice","").toString()

        val device = DeviceModel(id,nama,longitude,atitude,readmode,read,write,userDevice)

        refdevice.child(id).setValue(device).addOnCompleteListener {

        }
    }
}
