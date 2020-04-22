package com.example.whitecube

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profil.*

class ProfilActivity : AppCompatActivity() {

    private val TAG = ProfilActivity::getLocalClassName.toString()
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var  biometricManager: BiometricManager


    private lateinit var mGoogleSignInClient : GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        biometricManager = BiometricManager.from(this)
        val eksekutor = ContextCompat.getMainExecutor(this)
        cekBiometrikStatus(biometricManager)

        biometricPrompt = BiometricPrompt(this,eksekutor,
            object : BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showToast("Authentication Error $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    goToLogin()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showToast("Authentication Failed ")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometrik awek")
            .setDescription("Coba Coba Saja")
            .setNegativeButtonText("cancel")
            .build()



        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInAccount: GoogleSignInAccount? =  GoogleSignIn.getLastSignedInAccount(this)

        if(signInAccount != null){
            nama.setText(signInAccount.givenName)
            email.setText(signInAccount.email)
            Glide.with(this).load(signInAccount.photoUrl.toString()).into(foto_profil)

        }

        btn_logout.setOnClickListener {
            signOut()
        }
        btn_fingerprint.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
        btn_QRCode.setOnClickListener {
            val intent = Intent(this, MenuQRCode::class.java)
            startActivity(intent)
        }

    }
    private fun  signOut(){
        mGoogleSignInClient.signOut().addOnCompleteListener {
            Toast.makeText(this,"Signed Out Successfully", Toast.LENGTH_LONG).show()
        }
    }

    //biometric
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
        Toast.makeText(applicationContext,pesan,Toast.LENGTH_LONG).show()
    }

    private fun goToLogin(){
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
}
