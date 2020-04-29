package com.example.whitecube.Keamanan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whitecube.LoginRegister.RegisterUser
import com.example.whitecube.LoginRegister.RegisterUserKode2
import com.example.whitecube.R
import kotlinx.android.synthetic.main.activity_unique_password.*

class EditKodeKeamanan : AppCompatActivity() {

    private lateinit var kodekeamanan : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unique_password)

        kodekeamanan = ""

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
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun setKode(kd : String){
        if(kd == "hapus"){
            if(kodekeamanan.isNotEmpty()){
                kodekeamanan = kodekeamanan.substring(0,kodekeamanan.length-1)
                kode.setText(kodekeamanan)
            }
        }else{
            if(kodekeamanan.length < 6){
                kodekeamanan = kodekeamanan+kd
                kode.setText(kodekeamanan)
                if(kodekeamanan.length == 6){
                    editKodeKeamanan2()
                }
            }
        }
    }

    private fun editKodeKeamanan2(){
        val intent = Intent(this, EditKodeKeamanan2::class.java)
        intent.putExtra("kode1", kodekeamanan)
        startActivity(intent)
        finish()
    }
}
