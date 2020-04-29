package com.example.whitecube.LoginRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whitecube.R
import kotlinx.android.synthetic.main.activity_register_user.*
import kotlinx.android.synthetic.main.activity_unique_password.*

class RegisterUserKode : AppCompatActivity() {

    private lateinit var nama : String
    private lateinit var kodekeamanan : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unique_password)

        kodekeamanan = ""
        nama = intent.getStringExtra("nama").toString()
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
            val intent = Intent(this, RegisterUser::class.java)
            intent.putExtra("nama" , nama)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, RegisterUser::class.java)
        intent.putExtra("nama" , nama)
        startActivity(intent)
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
                    goRegisterKode2()
                }
            }
        }
    }

    private fun goRegisterKode2(){
        val intent = Intent(this, RegisterUserKode2::class.java)
        intent.putExtra("nama", nama)
        intent.putExtra("kode1", kodekeamanan)
        startActivity(intent)
        finish()
    }
}
