package com.example.whitecube.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whitecube.Adapter.RelayAdapter
import com.example.whitecube.GantiDevice.GantiDevice
import com.example.whitecube.GantiDevice.OnItemClickListener
import com.example.whitecube.GantiDevice.PilihDevice
import com.example.whitecube.GantiDevice.RecyclerAdapter
import com.example.whitecube.MainActivity
import com.example.whitecube.Model.*
import com.example.whitecube.R
import com.example.whitecube.Relay.AddRelay
import com.example.whitecube.Relay.DetailRelay
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_ganti_device.*
import kotlinx.android.synthetic.main.activity_register_user.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

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
        relays = arrayListOf()
        tambahPerangkat = view.findViewById(R.id.tambahPerangkat)

        tambahPerangkat.setOnClickListener {
            startActivity(Intent(view.context,AddRelay::class.java))
        }

        showRelay()
        rvRelay.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        adapter = RelayAdapter(view.context, relays)


        return view
    }

    private fun showRelay(){
        val query = refrelay.orderByChild("iddevice").equalTo(SP.getString("idDevice","").toString())
        Toast.makeText(activity!!.applicationContext,SP.getString("idDevice","").toString(),Toast.LENGTH_LONG).show()
        query.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                relays.clear()
                if(p0.exists()){
                    p0.children.forEach {
                        val value = it.getValue(RelayList::class.java)
                        if(value != null){
                            relays.add(value)
                        }
                    }
                    rvRelay.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

        })
    }


}