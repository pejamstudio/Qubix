package com.example.whitecube.Keamanan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.whitecube.LoginRegister.LoginActivity
import com.example.whitecube.LoginRegister.RegisterUserKode2
import com.example.whitecube.MainActivity
import com.example.whitecube.ProfilActivity
import com.example.whitecube.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_unique_password.*

class KodeKeamanan : AppCompatActivity() {

    private val TAG = ProfilActivity::getLocalClassName.toString()
    private lateinit var tipe : String
    private lateinit var SP : SharedPreferences
    private lateinit var auth : FirebaseAuth
    private lateinit var kdkeamanan: String
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var biometricManager: BiometricManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unique_password)

        //setcontent
        header.visibility = View.INVISIBLE
        gantiAkun.visibility = View.VISIBLE
        //end setcontent
        kdkeamanan = ""
        SP = getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()
        tipe = intent.getStringExtra("tipe").toString()

        biometricManager = BiometricManager.from(this)
        val eksekutor = ContextCompat.getMainExecutor(this)
        cekBiometrikStatus(biometricManager)

        biometricPrompt = BiometricPrompt(this,eksekutor,
            object : BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    goNext(tipe)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("QUBIX")
            .setDescription("Konfirmasi sidik jari")
            .setNegativeButtonText("Cancel")
            .build()

        if(tipe != "kodekeamanan"){
            biometricPrompt.authenticate(promptInfo)
            gantiAkun.visibility = View.INVISIBLE
        }


        gantiAkun.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            clearSP()
            finish()
        }

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


    }



    private fun cekBiometrikStatus(biometricManager: BiometricManager){
        when(biometricManager.canAuthenticate()){
            BiometricManager.BIOMETRIC_SUCCESS ->
                Log.d(TAG, "cekBiometrikStatus : aplikasi bisa menggunakan biometrik auth")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e(TAG, "cekBiometrikStatus : device ini tidak mendukung biometrik auth")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e(TAG, "cekBiometrikStatus : user tidak menggunakan biometrik konfigurasi")
        }

    }

    private fun showToast(pesan: String){
        Toast.makeText(applicationContext,pesan, Toast.LENGTH_SHORT).show()
    }

    private fun setKode(kd : String){
        if(kd == "hapus"){
            if(kdkeamanan.isNotEmpty()){
                kdkeamanan = kdkeamanan.substring(0,kdkeamanan.length-1)
                kode.setText(kdkeamanan)
            }
        }else{
            if(kdkeamanan.length < 6){
                kdkeamanan = kdkeamanan+kd
                kode.setText(kdkeamanan)
                if(kdkeamanan.length == 6){
                    if(kdkeamanan == SP.getString("kodeKeamanan","").toString()){
                        goNext(tipe)
                    }else{
                        showToast("Kode keamanan salah")
                        kdkeamanan = ""
                        kode.setText(kdkeamanan)
                    }
                }
            }
        }
    }

    private fun goNext(input : String){
        if(input == "kodekeamanan"){
            startActivity(Intent(this,EditKodeKeamanan::class.java))
            finish()
        }else if (input == "main"){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun clearSP(){
        val editor = SP.edit()
        editor.putString("id","")
        editor.putString("nama", "")
        editor.putString("email", "")
        editor.putString("idDevice","")
        editor.putString("modeUser","")
        editor.putString("namaDevice", "")
        editor.putString("lokasiDevice", "")
        editor.putString("longitude", "")
        editor.putString("atitude", "")
        editor.putString("kodeKeamanan", "")
        editor.apply()
    }
}
