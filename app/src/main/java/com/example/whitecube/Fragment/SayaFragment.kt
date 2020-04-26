package com.example.whitecube.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.whitecube.CameraScan
import com.example.whitecube.EditProfil.EditProfil
import com.example.whitecube.GantiDevice.GantiDevice
import com.example.whitecube.LoginActivity
import com.example.whitecube.Model.UserModel
import com.example.whitecube.PengaturanDevice.PengaturanDevice
import com.example.whitecube.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_camera_scan.*
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.fragment_saya.*

class SayaFragment : Fragment(){

    private lateinit var ref : DatabaseReference
    private lateinit var SP : SharedPreferences
    private lateinit var auth : FirebaseAuth
    private lateinit var editProfil: RelativeLayout
    private lateinit var editDevice: RelativeLayout
    private lateinit var tentang: RelativeLayout
    private lateinit var pengaturan: RelativeLayout
    private lateinit var add: ImageView
    private lateinit var logout : Button
    private lateinit var nama : TextView
    private lateinit var email : TextView
    private lateinit var labelPengaturanDevice: TextView
    private lateinit var foto : CircleImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_saya, container, false)
        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("user")
        SP = activity!!.getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        editProfil = view.findViewById(R.id.editProfil)
        editDevice = view.findViewById(R.id.editDevice)
        tentang = view.findViewById(R.id.tentang)
        pengaturan = view.findViewById(R.id.pengaturan)
        logout = view.findViewById(R.id.btn_logout)
        nama = view.findViewById(R.id.namaUser)
        email = view.findViewById(R.id.emailUser)
        foto = view.findViewById(R.id.fotoUser)
        add = view.findViewById(R.id.btn_add2)
        labelPengaturanDevice = view.findViewById(R.id.labelPengaturanDevice)

        setProfilContent()


        add.setOnClickListener {
            startActivity(Intent(view.context,GantiDevice::class.java))
        }
        editProfil.setOnClickListener {
            startActivity(Intent(view.context,EditProfil::class.java))
        }
        editDevice.setOnClickListener {
            startActivity(Intent(view.context,PengaturanDevice::class.java))
        }
        tentang.setOnClickListener {

        }
        pengaturan.setOnClickListener {

        }
        logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(view.context,LoginActivity::class.java))
            clearSP()
            activity!!.finish()
        }

        return view
    }


    private fun setProfilContent(){
        nama.text = SP.getString("nama","").toString()
        email.text = SP.getString("email","").toString()
        Glide.with(this).load(auth.currentUser!!.photoUrl.toString()).into(foto)
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
        editor.apply()
    }



//    private fun realTime(){
//        val query = ref.orderByChild("email").equalTo(auth.currentUser!!.email.toString())
//
//        query.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onDataChange(akun: DataSnapshot) {
//                if(akun.exists()){
//                    for(c in  akun.children){
//                        val data = c.getValue(UserModel::class.java)
//                        nama.text = data!!.nama
//                        email.text = data.email
//                    }
//
//                }
//            }
//        })
//    }
}