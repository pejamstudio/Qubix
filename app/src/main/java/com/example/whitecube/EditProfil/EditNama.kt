package com.example.whitecube.EditProfil

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.whitecube.MainActivity
import com.example.whitecube.Model.UserModel
import com.example.whitecube.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_profil.*
import kotlinx.android.synthetic.main.activity_register_user.*

class EditNama : AppCompatActivity() {

    private lateinit var ref : DatabaseReference
    private lateinit var SP : SharedPreferences
    private lateinit var nama: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        SP =getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        ref = FirebaseDatabase.getInstance().getReference("user")
        nama = findViewById(R.id.namaUser)
        nama.setText(SP.getString("nama","").toString())
        btn_back.setOnClickListener {
            this.finish()
        }
        btn_save.setOnClickListener{
            if(nama.text.toString().equals("")){
                nama.error = "Masukkan nama"
                nama.requestFocus()
            }else{
                editNama()
            }
        }
    }

    private fun editNama(){
        val user = UserModel(SP.getString("id","").toString(),nama.text.toString(),SP.getString("email","").toString())
        val editor = SP.edit()
        editor.putString("nama",nama.text.toString())
        editor.apply()
        ref.child(SP.getString("id","").toString()).setValue(user).addOnCompleteListener {
            this.finish()
        }
    }
}
