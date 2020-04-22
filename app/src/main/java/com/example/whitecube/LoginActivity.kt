package com.example.whitecube

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.ActionMode
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.whitecube.Model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_camera_scan.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    private lateinit var SP : SharedPreferences
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
        ref = FirebaseDatabase.getInstance().getReference("user")
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
                    val user = auth.currentUser
                    cekEmail(auth.currentUser!!.email.toString())
                    //

                } else {
                    Toast.makeText(this,"Sorry Auth Failed", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun cekEmail(email : String){
        val query = FirebaseDatabase.getInstance().getReference("pengguna").orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(email: DataSnapshot) {
                if(email.exists()){
                    getProfilContent()
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                }else{
                    startActivity(Intent(applicationContext,RegisterUser::class.java))
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
                            editor.putString("nama", data!!.nama)
                            editor.putString("nama", data!!.email)
                            editor.apply()

                        }
                    }

                }
            })
        }
    }
}
