package com.example.whitecube.Fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whitecube.Adapter.RelayAdapter
import com.example.whitecube.CameraScan
import com.example.whitecube.EditProfil.EditProfil
import com.example.whitecube.GantiDevice.GantiDevice
import com.example.whitecube.GantiDevice.OnItemClickListener
import com.example.whitecube.GantiDevice.PilihDevice
import com.example.whitecube.GantiDevice.RecyclerAdapter
import com.example.whitecube.MainActivity
import com.example.whitecube.Model.*
import com.example.whitecube.PengaturanDevice.EditLokasiDevice
import com.example.whitecube.R
import com.example.whitecube.Relay.AddRelay
import com.example.whitecube.Relay.DetailRelay
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_ganti_device.*
import kotlinx.android.synthetic.main.activity_register_user.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.random.nextInt

class HomeFragment : Fragment() {


    private lateinit var auth : FirebaseAuth
    private lateinit var textLokasi : TextView
    private lateinit var fotoUser : CircleImageView
    private lateinit var deviceAda : ScrollView
    private lateinit var deviceTidakAda : RelativeLayout
    private lateinit var lokasiAda : LinearLayout
    private lateinit var lokasiTidakAda : RelativeLayout
    private lateinit var suhu : TextView
    private lateinit var kelembapan : TextView
    private lateinit var tekanan : TextView
    private lateinit var namaDevice : TextView
    private lateinit var btn_add : ImageView
    private lateinit var tambahPerangkat : ImageView
    private lateinit var rvRelay : RecyclerView
    private lateinit var SP : SharedPreferences
    private lateinit var refrelay : DatabaseReference
    private lateinit var refdevice : DatabaseReference
    private lateinit var relays: ArrayList<RelayList>
    private lateinit var adapter: RelayAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)
        SP = activity!!.getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        refrelay = FirebaseDatabase.getInstance().getReference("relay")
        refdevice = FirebaseDatabase.getInstance().getReference("device")
        rvRelay = view.findViewById(R.id.rvRelay)
        auth = FirebaseAuth.getInstance()
        relays = arrayListOf()
        suhu = view.findViewById(R.id.suhu)
        kelembapan = view.findViewById(R.id.kelembapan)
        tekanan = view.findViewById(R.id.tekanan)
        tambahPerangkat = view.findViewById(R.id.tambahPerangkat)
        btn_add = view.findViewById(R.id.btn_add)
        namaDevice = view.findViewById(R.id.namaDevice)
        deviceAda = view.findViewById(R.id.deviceAda)
        deviceTidakAda = view.findViewById(R.id.deviceTidakAda)
        lokasiAda = view.findViewById(R.id.lokasiAda)
        lokasiTidakAda = view.findViewById(R.id.lokasiTidakAda)
        fotoUser = view.findViewById(R.id.fotoUser)
        textLokasi = view.findViewById(R.id.textLokasi)

        // set content
        if(SP.getString("idDevice","").toString() != ""){
            deviceAda.visibility = View.VISIBLE
            deviceTidakAda.visibility = View.GONE
            namaDevice.visibility = View.VISIBLE
            namaDevice.text = SP.getString("namaDevice","").toString()
            fotoUser.visibility = View.GONE
            if(SP.getString("longitude","").toString() != ""){

                lokasiAda.visibility = View.VISIBLE
                lokasiTidakAda.visibility = View.GONE
                getWeather()
            }else{
                lokasiAda.visibility = View.GONE
                lokasiTidakAda.visibility = View.VISIBLE
            }
        }else{
            namaDevice.visibility = View.GONE
            deviceAda.visibility = View.GONE
            deviceTidakAda.visibility = View.VISIBLE
            fotoUser.visibility = View.VISIBLE
            Glide.with(this).load(auth.currentUser!!.photoUrl.toString()).into(fotoUser)
        }

        if(SP.getString("modeUser","").toString()=="1"){
            tambahPerangkat.visibility = View.VISIBLE

        }else{
            tambahPerangkat.visibility = View.GONE
            if(SP.getString("longitude","").toString() == ""){
                textLokasi.text = "Lokasi belum diatur"
            }
        }

        //end set content

        fotoUser.setOnClickListener {
            startActivity(Intent(view.context,EditProfil::class.java))
        }

        deviceTidakAda.setOnClickListener {
            startActivity(Intent(view.context,CameraScan::class.java))
            activity!!.finish()
        }

        lokasiTidakAda.setOnClickListener {
            if(SP.getString("longitude","").toString() != ""){
                startActivity(Intent(view.context,EditLokasiDevice::class.java))
            }
        }

        namaDevice.setOnClickListener {
            startActivity(Intent(view.context,GantiDevice::class.java))
            activity!!.finish()
        }

        btn_add.setOnClickListener {
            startActivity(Intent(view.context,CameraScan::class.java))
        }

        tambahPerangkat.setOnClickListener {
            startActivity(Intent(view.context,AddRelay::class.java))
            activity!!.finish()
        }

        showRelay()
        rvRelay.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        adapter = RelayAdapter(view.context, relays)


        return view
    }




    private fun showRelay(){
        val query = refrelay.orderByChild("tempatrelay")
        query.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                relays.clear()
                if(p0.exists()){
                    p0.children.forEach {
                        val value = it.getValue(RelayList::class.java)
                        if(value != null){
                            if(value.iddevice.contains(SP.getString("idDevice","").toString())){
                                relays.add(value)
                            }
                        }
                    }
                    rvRelay.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

        })
    }

    private fun getWeather(){
        val cal = Calendar.getInstance()
        val formatter = SimpleDateFormat("HH")
        val waktu = formatter.format(cal.time).toInt()

        when (waktu) {
            in 0..5 -> {
                suhu.text = Random.nextInt(26..27).toString()+" °C"
                kelembapan.text = Random.nextInt(72..74).toString()+".0 %"
                tekanan.text = "1007."+Random.nextInt(2..7).toString()+" hPa"
            }
            in 6..10 -> {
                suhu.text = Random.nextInt(27..28).toString()+" °C"
                kelembapan.text = Random.nextInt(68..70).toString()+".0 %"
                tekanan.text = "1008."+Random.nextInt(2..8).toString()+" hPa"
            }
            in 11..14 -> {
                suhu.text = Random.nextInt(29..30).toString()+" °C"
                kelembapan.text = Random.nextInt(64..66).toString()+".0 %"
                tekanan.text = "1009."+Random.nextInt(2..7).toString()+" hPa"
            }
            in 15..18 -> {
                suhu.text = Random.nextInt(27..28).toString()+" °C"
                kelembapan.text = Random.nextInt(68..70).toString()+".0 %"
                tekanan.text = "1008."+Random.nextInt(2..8).toString()+" hPa"
            }
            in 19..24 -> {
                suhu.text = Random.nextInt(26..27).toString()+" °C"
                kelembapan.text = Random.nextInt(74..76).toString()+".0 %"
                tekanan.text = "1007."+Random.nextInt(2..7).toString()+" hPa"
            }
        }



    }


}