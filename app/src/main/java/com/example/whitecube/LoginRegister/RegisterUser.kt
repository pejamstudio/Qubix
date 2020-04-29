package com.example.whitecube.LoginRegister

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whitecube.MainActivity
import com.example.whitecube.Model.UserModel
import com.example.whitecube.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register_user.*

class RegisterUser : AppCompatActivity() {

    private lateinit var SP : SharedPreferences
    private lateinit var ref : DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        auth = FirebaseAuth.getInstance()
        SP =  getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        ref = FirebaseDatabase.getInstance().getReference("user")

        if(intent.getStringExtra("nama").toString()!= null){
            namaUser.setText(intent.getStringExtra("nama").toString())
        }
        btn_save.setOnClickListener {
            if(namaUser.text.toString().equals("")){
                namaUser.error = "Masukkan nama"
                namaUser.requestFocus()
            }else{
                val intent = Intent(this, RegisterUserKode::class.java)
                intent.putExtra("nama", namaUser.text.toString())
                startActivity(intent)
                finish()
            }
        }
        btn_back.setOnClickListener {
            auth.signOut()
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        auth.signOut()
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
    }

}
