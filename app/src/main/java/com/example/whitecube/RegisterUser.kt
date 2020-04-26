package com.example.whitecube

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whitecube.Model.UserModel
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
        btn_save.setOnClickListener {
            if(namaUser.text.toString().equals("")){
                namaUser.error = "Masukkan nama"
                namaUser.requestFocus()
            }else{
                createAkun()
            }
        }
        btn_back.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
    private fun createAkun(){

        val userId = ref.push().key.toString()

        val user = UserModel(userId,namaUser.text.toString(),auth.currentUser!!.email.toString())

        ref.child(userId).setValue(user).addOnCompleteListener {
            getProfilContent()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    private fun getProfilContent(){
        if(auth.currentUser != null){
            val query = ref.orderByChild("email").equalTo(auth.currentUser!!.email.toString())

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(akun: DataSnapshot) {
                    if(akun.exists()){
                        for(c in  akun.children){
                            val data = c.getValue(UserModel::class.java)
                            val editor = SP.edit()
                            editor.putString("id",data!!.id)
                            editor.putString("nama", data!!.nama)
                            editor.putString("email", data!!.email)
                            editor.apply()

                        }
                    }

                }
            })
        }
    }
}
