package com.example.whitecube

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whitecube.LoginRegister.LoginActivity

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val backgrond = object : Thread(){
            override fun run() {
                try {
                    java.lang.Thread.sleep(500)
                    //tambah loading
                    startActivity(Intent(applicationContext,LoginActivity::class.java))
                    finish()
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        backgrond.start()
    }
}
