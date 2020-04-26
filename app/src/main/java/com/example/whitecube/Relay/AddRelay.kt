package com.example.whitecube.Relay


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.whitecube.Model.RelayModel
import com.example.whitecube.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_detail_relay.*
import kotlinx.android.synthetic.main.activity_edit_relay.*
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog


class AddRelay : AppCompatActivity() {

    private lateinit var SP : SharedPreferences
    private lateinit var refrelay : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_relay)


        refrelay = FirebaseDatabase.getInstance().getReference("relay")
        SP = getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        val editor = SP.edit()
        editor.putString("irKode", "")
        editor.apply()
        setContent()

        irkode.setOnClickListener {
            startActivity(Intent(this,ReadIrRemote::class.java))
        }

        btn_save.setOnClickListener {
            if(namarelay.text.toString().equals("")){
                namarelay.error = "Masukkan nama relay"
                namarelay.requestFocus()
            }else{
                addRelay()
            }
        }

        btn_back.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Tambah Perangkat")
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

        val adapterJenis = ArrayAdapter(this, com.example.whitecube.R.layout.support_simple_spinner_dropdown_item,SpinnerData.jenis)
        val adapterTempat = ArrayAdapter(this, com.example.whitecube.R.layout.support_simple_spinner_dropdown_item,SpinnerData.tempat)

        jenisRelay.adapter = adapterJenis
        tempatRelay.adapter = adapterTempat
        jenisRelay.setSelection(adapterJenis.getPosition("Lainnya"))
        tempatRelay.setSelection(adapterTempat.getPosition("Lainnya"))


    }

    override fun onResume() {
        super.onResume()
        setContent()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Tambah Perangkat")
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


    private fun addRelay(){
        val id = refrelay.push().key.toString()
        if(irkode.text.equals("Atur IR kode")){
            Toast.makeText(this,"Atur IR kode terlebih dahulu",Toast.LENGTH_LONG).show()
        }else{
            val relay = RelayModel(id,SP.getString("idDevice","").toString(),"0",namarelay.text.toString(),jenisRelay.selectedItem.toString(),tempatRelay.selectedItem.toString(),"","","","",irkode.text.toString())
            refrelay.child(id).setValue(relay).addOnCompleteListener {
                finish()
            }
        }
    }

    private fun setContent(){

        modeSwitch.visibility = View.GONE
        modeRemote.visibility = View.VISIBLE
        if(SP.getString("irKode","").toString() !=""){
            irkode.text = SP.getString("irKode","").toString()
        }else{
            irkode.text = "Atur IR kode"
        }

    }
}
