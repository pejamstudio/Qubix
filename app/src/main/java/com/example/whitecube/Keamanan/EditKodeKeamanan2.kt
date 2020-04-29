package com.example.whitecube.Keamanan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whitecube.LoginRegister.RegisterUserKode
import com.example.whitecube.MainActivity
import com.example.whitecube.Model.UserModel
import com.example.whitecube.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_unique_password.*

class EditKodeKeamanan2 : AppCompatActivity() {

    private lateinit var SP : SharedPreferences
    private lateinit var ref : DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var kodekeamanan1 : String
    private lateinit var kodekeamanan2 : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unique_password)

        auth = FirebaseAuth.getInstance()
        SP =  getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        ref = FirebaseDatabase.getInstance().getReference("user")
        kodekeamanan1 = intent.getStringExtra("kode1").toString()
        kodekeamanan2 = ""

        //set konten
        textHeader.text = "Konfirmasi Kode"
        labelKode.text = "Masukkan konfirmasi kode keamanan"

        satu.setOnClickListener {setKode("1") }
        dua.setOnClickListener {setKode("2") }
        tiga.setOnClickListener {setKode("3") }
        empat.setOnClickListener {setKode("4") }
        lima.setOnClickListener {setKode("5") }
        enam.setOnClickListener {setKode("6") }
        tujuh.setOnClickListener {setKode("7") }
        delapan.setOnClickListener {setKode("8") }
        sembilan.setOnClickListener {setKode("9") }
        nol.setOnClickListener {setKode("0") }
        hapus.setOnClickListener {setKode("hapus") }
        back.setOnClickListener {
            val intent = Intent(this, EditKodeKeamanan::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, EditKodeKeamanan::class.java)
        startActivity(intent)
        finish()
    }

    private fun setKode(kd : String){
        if(kd == "hapus"){
            if(kodekeamanan2.isNotEmpty()){
                kodekeamanan2 = kodekeamanan2.substring(0,kodekeamanan2.length-1)
                kode.setText(kodekeamanan2)
            }
        }else{
            if(kodekeamanan2.length < 6){
                kodekeamanan2 = kodekeamanan2+kd
                kode.setText(kodekeamanan2)
                if(kodekeamanan2.length == 6){
                    if(kodekeamanan1 == kodekeamanan2){
                        Toast.makeText(this,"Kode keamanan berhasil diatur", Toast.LENGTH_SHORT).show()
                        editKode(kodekeamanan2)
                    }else{
                        Toast.makeText(this,"Kode keamanan tidak cocok", Toast.LENGTH_SHORT).show()
                        kodekeamanan2 = ""
                        kode.setText(kodekeamanan2)
                    }
                }
            }
        }
    }

    private fun editKode(kode : String){

        val id = SP.getString("id","").toString()
        val nama = SP.getString("nama","").toString()
        val email = SP.getString("email","").toString()

        val user = UserModel(id,nama,email,kode)

        ref.child(id).setValue(user).addOnCompleteListener {
            val editor = SP.edit()
            editor.putString("kodeKeamanan",kode)
            editor.apply()
            finish()
        }
    }

}
