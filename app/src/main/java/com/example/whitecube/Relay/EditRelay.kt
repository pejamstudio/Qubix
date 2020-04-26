package com.example.whitecube.Relay

import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.whitecube.MainActivity
import com.example.whitecube.Model.RelayModel
import com.example.whitecube.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_detail_relay.*
import kotlinx.android.synthetic.main.activity_edit_relay.*
import java.util.*

class EditRelay : AppCompatActivity() {

    private lateinit var refrelay : DatabaseReference
    private lateinit var SP : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_relay)
        SP = getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        refrelay = FirebaseDatabase.getInstance().getReference("relay")

        setRelayContent()


        penjadwalanRelay.setOnClickListener {
            if(penjadwalanRelay.isChecked){
                jadwal.visibility = View.VISIBLE
                jadwalHidup.text = SP.getString("jadwalHidup","").toString()
                jadwalMati.text = SP.getString("jadwalMati","").toString()
            }else{
                jadwal.visibility = View.GONE
            }
        }

        irkode.setOnClickListener {
            startActivity(Intent(this,ReadIrRemote::class.java))
        }

        btn_save.setOnClickListener {
            if(namarelay.text.toString().equals("")){
                namarelay.error = "Masukkan nama relay"
                namarelay.requestFocus()
            }else{
                saveUpdateRelay()
            }
        }

        btn_back.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Pengaturan Perangkat")
            alertDialog.setMessage("Data tidak akan tersimpan. Apakah anda yakin ingin keluar ?")
                .setCancelable(false)
                .setPositiveButton("Iya", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, id: Int) {
                        finish()
                    }
                })

                .setNegativeButton("Tidak", object: DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, id: Int) {
                        dialog?.cancel()
                    }
                }).create().show()
        }



        val adapterJenis = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,SpinnerData.jenis)
        val adapterTempat = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,SpinnerData.tempat)

        jenisRelay.adapter = adapterJenis
        tempatRelay.adapter = adapterTempat
        jenisRelay.setSelection(adapterJenis.getPosition(SP.getString("jenisRelay","").toString()))
        tempatRelay.setSelection(adapterTempat.getPosition(SP.getString("tempatRelay","").toString()))



        jadwalHidup.setOnClickListener {
            val cal = Calendar.getInstance()
            val timePicker = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                jadwalHidup.text = java.text.SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this,timePicker,SP.getString("jadwalHidup","").toString().substring(0,2).toInt(),SP.getString("jadwalHidup","").toString().substring(3,5).toInt(),true).show()
        }

        jadwalMati.setOnClickListener {
            val cal = Calendar.getInstance()
            val timePicker = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                jadwalMati.text = java.text.SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this,timePicker,SP.getString("jadwalMati","").toString().substring(0,2).toInt(),SP.getString("jadwalMati","").toString().substring(3,5).toInt(),true).show()
        }


    }

    override fun onResume() {
        super.onResume()
        setRelayContent()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Pengaturan Perangkat")
        alertDialog.setMessage("Data tidak akan tersimpan. Apakah anda yakin ingin keluar ?")
            .setCancelable(false)
            .setPositiveButton("Iya", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, id: Int) {
                    finish()
                }
            })

            .setNegativeButton("Tidak", object: DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, id: Int) {
                    dialog?.cancel()
                }
            }).create().show()
    }
    private fun setRelayContent(){
        namarelay.setText(SP.getString("namaRelay","").toString())
        if(SP.getString("relayMode","") == "1"){
            modeSwitch.visibility = View.VISIBLE
            modeRemote.visibility = View.GONE
            if(SP.getString("penjadwalanRelay","") == "1"){
                penjadwalanRelay.isChecked = true
                jadwal.visibility = View.VISIBLE
                jadwalHidup.text = SP.getString("jadwalHidup","").toString()
                jadwalMati.text = SP.getString("jadwalMati","").toString()
            }else{
                penjadwalanRelay.isChecked = false
                jadwal.visibility = View.GONE
            }
        }else{
            modeSwitch.visibility = View.GONE
            modeRemote.visibility = View.VISIBLE
            if(SP.getString("irKode","").toString() !=""){
                irkode.text = SP.getString("irKode","").toString()
            }else{
                irkode.text = "Atur IR kode"
            }
        }

    }

    private fun saveUpdateRelay(){
        var relay : RelayModel

        val id = SP.getString("idRelay","").toString()
        val iddevice = SP.getString("idDevice","").toString()
        val relayMode = SP.getString("relayMode","").toString()
        val statusRelay = SP.getString("statusRelay","").toString()
        val penjadwalanrelay = SP.getString("penjadwalanRelay","").toString()
        val jadwalhidup = SP.getString("jadwalHidup","").toString()
        val jadwalmati = SP.getString("jadwalMati","").toString()
        val irKode = SP.getString("irKode","").toString()
        if(relayMode == "1"){

            if(penjadwalanRelay.isChecked){
                relay = RelayModel(id,iddevice,relayMode,namarelay.text.toString(),jenisRelay.selectedItem.toString(),tempatRelay.selectedItem.toString(),statusRelay,"1",jadwalHidup.text.toString(),jadwalMati.text.toString(),irKode)
                val editor = SP.edit()
                editor.putString("namaRelay", relay.namarelay)
                editor.putString("jenisRelay", relay.jenisrelay)
                editor.putString("tempatRelay", relay.tempatrelay)
                editor.putString("penjadwalanRelay", relay.penjadwalan)
                editor.putString("jadwalHidup", relay.jadwalhidup)
                editor.putString("jadwalMati", relay.jadwalmati)
                editor.apply()
            }else{
                relay = RelayModel(id,iddevice,relayMode,namarelay.text.toString(),jenisRelay.selectedItem.toString(),tempatRelay.selectedItem.toString(),statusRelay,"0",jadwalhidup,jadwalmati,irKode)
                val editor = SP.edit()
                editor.putString("namaRelay", relay.namarelay)
                editor.putString("jenisRelay", relay.jenisrelay)
                editor.putString("tempatRelay", relay.tempatrelay)
                editor.putString("penjadwalanRelay", relay.penjadwalan)
                editor.apply()

            }
        }else{
            relay = RelayModel(id,iddevice,relayMode,namarelay.text.toString(),jenisRelay.selectedItem.toString(),tempatRelay.selectedItem.toString(),statusRelay,penjadwalanrelay,jadwalhidup,jadwalmati,irkode.text.toString())
            val editor = SP.edit()
            editor.putString("namaRelay", relay.namarelay)
            editor.putString("jenisRelay", relay.jenisrelay)
            editor.putString("tempatRelay", relay.tempatrelay)
            editor.putString("irKode", relay.irkode)
            editor.apply()
        }
        refrelay.child(id).setValue(relay).addOnCompleteListener {
            finish()
        }
    }


}
