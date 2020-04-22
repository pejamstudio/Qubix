package com.example.whitecube

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_qrscanner.*

class QRscanner : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscanner)
        if(intent.getStringExtra("activity") == "CamScan"){
            val qrcode = intent.getStringExtra("qrcode").toString()
            text_scan.setText(qrcode)
        }

        btn_scan.setOnClickListener {
            gotoCameraScan()
        }


    }

    private fun gotoCameraScan(){
        val intent = Intent(this,CameraScan::class.java)
        startActivity(intent)
    }


}
