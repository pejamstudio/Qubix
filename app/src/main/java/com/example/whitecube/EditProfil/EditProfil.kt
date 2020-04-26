package com.example.whitecube.EditProfil

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whitecube.R
import kotlinx.android.synthetic.main.activity_edit_profil.*

class EditProfil : AppCompatActivity() {

    private lateinit var SP : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)
        SP =getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)

        setProfileContent()

        backprofil.setOnClickListener {
            this.finish()
        }
        editnama.setOnClickListener {
            startActivity(Intent(this,EditNama::class.java))
        }
    }

    private fun setProfileContent(){
        namaUser.text = SP.getString("nama","").toString()
        emailUser.text = SP.getString("email","").toString()
    }

    override fun onResume() {
        super.onResume()
        setProfileContent()
    }
}
