package com.example.whitecube.PengaturanDevice

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whitecube.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_berbagi_device.*

class BerbagiDevice : AppCompatActivity() {

    private lateinit var SP : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_berbagi_device)
        SP = getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)

        generateQR(SP.getString("idDevice","").toString())

        btn_back.setOnClickListener {
            finish()
        }
    }
    private fun generateQR(kode: String){
        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWriter.encode(kode,BarcodeFormat.QR_CODE,300,300)
            val barcoodeEncoder = BarcodeEncoder()
            val bitmap = barcoodeEncoder.createBitmap(bitMatrix)
            qrImage.setImageBitmap(bitmap)
        }catch (e: WriterException){
            e.printStackTrace()
        }
    }
}
