package com.example.whitecube.LoginRegister

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whitecube.MainActivity
import com.example.whitecube.Model.UserModel
import com.example.whitecube.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register_user.*
import kotlinx.android.synthetic.main.activity_unique_password.*

class RegisterUserKode2 : AppCompatActivity() {

    private lateinit var SP : SharedPreferences
    private lateinit var ref : DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var nama : String
    private lateinit var kodekeamanan1 : String
    private lateinit var kodekeamanan2 : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unique_password)

        auth = FirebaseAuth.getInstance()
        SP =  getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        ref = FirebaseDatabase.getInstance().getReference("user")
        nama = intent.getStringExtra("nama").toString()
        kodekeamanan1 = intent.getStringExtra("kode1").toString()
        kodekeamanan2 = ""

        //setkontent
        textHeader.text = "Konfirmasi Kode"

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
        back.setOnClickListener {
            val intent = Intent(this, RegisterUserKode::class.java)
            intent.putExtra("nama" , nama)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, RegisterUserKode::class.java)
        intent.putExtra("nama" , nama)
        startActivity(intent)
        finish()
    }
    private fun setKode(kd : String){
        if(kd == "hapus"){
            if(kodekeamanan2.isNotEmpty()){
                kodekeamanan2 = kodekeamanan2.substring(0,kodekeamanan2.length-1)
                kode.setText(kodekeamanan2)
            }
        }else{
            if(kodekeamanan2.length < 6){
                kodekeamanan2 = kodekeamanan2+kd
                kode.setText(kodekeamanan2)
                if(kodekeamanan2.length == 6){
                    if(kodekeamanan1 == kodekeamanan2){
                        Toast.makeText(this,"Akun berhasil dibuat", Toast.LENGTH_SHORT).show()
                        createAkun()
                    }else{
                        Toast.makeText(this,"Kode keamanan tidak cocok", Toast.LENGTH_SHORT).show()
                        kodekeamanan2 = ""
                        kode.setText(kodekeamanan2)
                    }
                }
            }
        }
    }

    private fun createAkun(){

        val userId = ref.push().key.toString()

        val user = UserModel(userId,nama,auth.currentUser!!.email.toString(),kodekeamanan2)

        ref.child(userId).setValue(user).addOnCompleteListener {
            getProfilContent()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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
                            editor.putString("kodeKeamanan", data!!.kodekeamanan)
                            editor.apply()

                        }
                    }

                }
            })
        }
    }
}
