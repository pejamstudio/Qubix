package com.example.whitecube

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu_qrcode.*

class MenuQRCode : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_qrcode)

        btn_scanQR.setOnClickListener {
            val intent = Intent(this, QRscanner::class.java)
            intent.putExtra("activity","MenuQRCode")
            startActivity(intent)
        }
        btn_generateQR.setOnClickListener {
            val intent = Intent(this, QRGenerator::class.java)
            startActivity(intent)
        }
    }
}
