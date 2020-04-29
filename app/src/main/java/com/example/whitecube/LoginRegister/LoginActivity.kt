package com.example.whitecube.LoginRegister

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.whitecube.Keamanan.KodeKeamanan
import com.example.whitecube.MainActivity
import com.example.whitecube.Model.DeviceModel
import com.example.whitecube.Model.ModeUserModel
import com.example.whitecube.Model.UserModel
import com.example.whitecube.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    private lateinit var SP : SharedPreferences
    private lateinit var refmode : DatabaseReference
    private lateinit var refdevice : DatabaseReference
    private lateinit var ref : DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var mGoogleSignInClient : GoogleSignInClient
    private val RC_SIGN_IN: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loding.visibility = View.GONE
        bg_loding.visibility = View.GONE
        SP = getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()
        refdevice = FirebaseDatabase.getInstance().getReference("device")
        refmode = FirebaseDatabase.getInstance().getReference("modeuser")
        ref = FirebaseDatabase.getInstance().getReference("user")
        cekLoginStatus()
        createRequest()
        findViewById<TextView>(R.id.signin_google).setOnClickListener {
            signIn()
        }
    }



    private fun createRequest(){
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    private fun signIn() {

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)


    }



    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {

            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {


        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    loding.visibility = View.GONE
                    bg_loding.visibility = View.GONE
                    cekEmail(auth.currentUser!!.email.toString())
                    //

                } else {
                    Toast.makeText(this,"Sorry Auth Failed", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun cekEmail(email : String){
        val query = ref.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(email: DataSnapshot) {
                if(email.exists()){
                    finish()
                    getProfilContent()
                    val intent = Intent(applicationContext,KodeKeamanan::class.java)
                    intent.putExtra("tipe","main")
                    startActivity(intent)
                }else{
                    finish()
                    val intent = Intent(applicationContext, RegisterUser::class.java)

                    intent.putExtra("nama","")
                    startActivity(intent)
                }
            }
        })
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
                            editor.putString("nama", data.nama)
                            editor.putString("email", data.email)
                            editor.putString("kodeKeamanan", data.kodekeamanan)
                            editor.apply()
                            getModeData(data.id)
                        }

                    }
                }
            })
        }
    }

    private fun getModeData(idUser: String){
        val query = refmode.orderByChild("iduser").limitToFirst(1).equalTo(idUser)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(akun: DataSnapshot) {
                if(akun.exists()){
                    for(c in  akun.children){
                        val data = c.getValue(ModeUserModel::class.java)
                        val editor = SP.edit()
                        editor.putString("idDevice",data!!.iddevice)
                        editor.putString("modeUser", data.mode)
                        editor.apply()
                        getDeviceData(data.iddevice)
                    }

                }else{
                    val editor = SP.edit()
                    editor.putString("idDevice","")
                    editor.putString("modeUser","")
                    editor.putString("namaDevice", "")
                    editor.putString("longitude", "")
                    editor.putString("atitude", "")
                    editor.putString("readMode", "")
                    editor.putString("read", "")
                    editor.putString("write", "")
                    editor.putString("userDevice", "")
                    editor.apply()

                }
            }
        })
    }
    private fun getDeviceData(idDevice : String){
        val query = refdevice.orderByChild("id").equalTo(idDevice)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(akun: DataSnapshot) {
                if(akun.exists()){
                    for(c in  akun.children){
                        val data = c.getValue(DeviceModel::class.java)
                        val editor = SP.edit()
                        editor.putString("namaDevice", data!!.nama)
                        editor.putString("longitude", data.longitude)
                        editor.putString("atitude", data.atitude)
                        editor.putString("readMode", data.readmode)
                        editor.putString("read",data.read)
                        editor.putString("write", data.write)
                        editor.putString("userDevice", data.user)
                        editor.apply()

                    }

                }else{
                    val editor = SP.edit()
                    editor.putString("namaDevice", "")
                    editor.putString("longitude", "")
                    editor.putString("atitude", "")
                    editor.putString("readMode", "")
                    editor.putString("read", "")
                    editor.putString("write", "")
                    editor.putString("userDevice", "")
                    editor.apply()
                }
            }
        })
    }

    private fun cekLoginStatus(){
        if(SP.getString("email","").toString() != ""){
            val intent = Intent(this,KodeKeamanan::class.java)
            intent.putExtra("tipe","main")
            startActivity(intent)
            finish()
        }
    }
}
