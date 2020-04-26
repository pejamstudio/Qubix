package com.example.whitecube.Relay

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.whitecube.Model.DeviceModel
import com.example.whitecube.Model.ModeUserModel
import com.example.whitecube.Model.RelayModel
import com.example.whitecube.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detail_relay.*
import kotlinx.android.synthetic.main.activity_edit_relay.*

class DetailRelay : AppCompatActivity() {


    private lateinit var SP : SharedPreferences
    private lateinit var refrelay : DatabaseReference
    private lateinit var refdevice : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_relay)
        SP = getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        refrelay = FirebaseDatabase.getInstance().getReference("relay")
        refdevice = FirebaseDatabase.getInstance().getReference("device")
        val id = intent.getStringExtra("id").toString()

        getContentRelay(id)
        btn_edit.setOnClickListener {
            startActivity(Intent(this,EditRelay::class.java))
        }

        btn_back1.setOnClickListener {
            finish()
        }

        goRemote.setOnClickListener {
            if(SP.getString("irKode" ,"").toString()!= ""){
                gRemote(SP.getString("irKode" ,"").toString())
            }else{
                Toast.makeText(this,"IR kode belum diatur",Toast.LENGTH_SHORT).show()
            }
        }

        switchRelay.setOnClickListener {
            if(switchRelay.isChecked){
                sRelay("1")
            }else{
                sRelay("0")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getContentRelay(SP.getString("idRelay","").toString())
    }

    private fun sRelay(status : String){
        val id = SP.getString("idRelay","").toString()
        val iddevice = SP.getString("idDevice","").toString()
        val relayMode = SP.getString("relayMode","").toString()
        val namaRelay = SP.getString("namaRelay","").toString()
        val jenisRelay = SP.getString("jenisRelay","").toString()
        val tempatRelay = SP.getString("tempatRelay","").toString()
        val penjadwalanRelay = SP.getString("penjadwalanRelay","").toString()
        val jadwalHidup = SP.getString("jadwalHidup","").toString()
        val jadwalMati = SP.getString("jadwalMati","").toString()
        val irKode = SP.getString("irKode","").toString()

        val relay = RelayModel(id,iddevice,relayMode,namaRelay,jenisRelay,tempatRelay,status,penjadwalanRelay,jadwalHidup,jadwalMati,irKode)
        refrelay.child(id).setValue(relay).addOnCompleteListener {
            val editor = SP.edit()
            editor.putString("statusRelay", status)
            editor.apply()
        }

    }

    private fun gRemote(irkode:String){
        val id = SP.getString("idDevice","").toString()
        val readMode = SP.getString("readMode","").toString()
        val read = SP.getString("read","").toString()
        val longitude = SP.getString("longitude","").toString()
        val atitude = SP.getString("atitude","").toString()
        val userDevice = SP.getString("userDevice","").toString()
        val nama = SP.getString("namaDevice","").toString()

        val device = DeviceModel(id,nama,longitude,atitude,readMode,read,irkode,userDevice)
        refdevice.child(id).setValue(device)

    }
    private fun getContentRelay(id : String){
        val query = refrelay.orderByChild("id").limitToFirst(1).equalTo(id)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(akun: DataSnapshot) {
                if(akun.exists()){
                    for(c in  akun.children){
                        val relay = c.getValue(RelayModel::class.java)
                        val editor = SP.edit()
                        editor.putString("idRelay", relay!!.id)
                        editor.putString("relayMode", relay.relaymode)
                        editor.putString("namaRelay", relay.namarelay)
                        editor.putString("jenisRelay", relay.jenisrelay)
                        editor.putString("tempatRelay", relay.tempatrelay)
                        editor.putString("statusRelay", relay.status)
                        editor.putString("penjadwalanRelay", relay.penjadwalan)
                        editor.putString("jadwalHidup", relay.jadwalhidup)
                        editor.putString("jadwalMati", relay.jadwalmati)
                        editor.putString("irKode", relay.irkode)
                        editor.apply()

                        if(SP.getString("modeUser","").toString()!="1"){
                            btn_edit.visibility = View.GONE
                        }

                        if(relay.relaymode == "1"){
                            switchRelay.visibility = View.VISIBLE
                            goRemote.visibility = View.GONE
                            if(relay.penjadwalan == "1"){
                                ketRemPen.text = relay.jadwalhidup+" - "+relay.jadwalmati
                            }else{
                                ketRemPen.text = "Tidak Aktif"
                            }
                        }else{
                            switchRelay.visibility = View.GONE
                            goRemote.visibility = View.VISIBLE
                            labelRemPen.text = "IR Kode"
                            if(relay.irkode != ""){
                                ketRemPen.text = relay.irkode
                            }else{
                                ketRemPen.text = "Belum Diatur"
                            }
                        }
                        if(relay.status == "1"){
                            switchRelay.isChecked == true
                        }else{
                            switchRelay.isChecked == false
                        }
                        namaRelay.text = relay.namarelay
                        jenisrelay.text = relay.jenisrelay
                        tempatrelay.text = relay.tempatrelay

                }
            }
            }
        })

    }


}
