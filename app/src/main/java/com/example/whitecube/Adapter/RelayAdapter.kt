package com.example.whitecube.Adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.whitecube.GantiDevice.PilihDevice
import com.example.whitecube.Model.RelayList
import com.example.whitecube.Model.RelayModel
import com.example.whitecube.R
import com.example.whitecube.Relay.DetailRelay
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.item_ganti_device.view.*
import kotlinx.android.synthetic.main.item_relay.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RelayAdapter(val context: Context, val List : ArrayList<RelayList>) :
    RecyclerView.Adapter<RelayAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelayAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_relay, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return List.size
    }

    inner class Holder(view: View?) : RecyclerView.ViewHolder(view!!),
        View.OnClickListener {

        val labelSwitchOrRemote: ImageView
        val jenisRelay: ImageView
        val namaRelay: TextView
        val tempatRelay: TextView

        init {
            labelSwitchOrRemote = view!!.findViewById(R.id.labelSwitchOrRemote) as ImageView
            jenisRelay = view!!.findViewById(R.id.gambarJenis) as ImageView
            namaRelay = view.findViewById(R.id.namaRelay) as TextView
            tempatRelay = view.findViewById(R.id.tempatRelay) as TextView
        }

        fun bind(list: RelayList, context: Context) {
            if(list.jenisrelay == "Lampu"){
                jenisRelay.setImageResource(R.drawable.lampu)
            }else if(list.jenisrelay == "Kipas Angin"){
                jenisRelay.setImageResource(R.drawable.kipas)
            }else if(list.jenisrelay == "AC"){
                jenisRelay.setImageResource(R.drawable.ac)
            }else if(list.jenisrelay == "TV"){
                jenisRelay.setImageResource(R.drawable.tv)
            }else if(list.jenisrelay == "Kulkas"){
                jenisRelay.setImageResource(R.drawable.kulkas)
            }else if(list.jenisrelay == "Pompa Air"){
                jenisRelay.setImageResource(R.drawable.pompa)
            }else{
                jenisRelay.setImageResource(R.drawable.lainnya)
            }

            if(list.relaymode == "1"){
                if(list.status == "1"){
                    labelSwitchOrRemote.setImageResource(R.drawable.ic_switch_nyala)
                }else{
                    labelSwitchOrRemote.setImageResource(R.drawable.ic_switch_mati)
                }
            }else{
                labelSwitchOrRemote.setImageResource(R.drawable.ic_settings_remote_black_24dp)
            }

            namaRelay.setText(list.namarelay)
            tempatRelay.setText(list.tempatrelay)

            itemView.setOnClickListener {
                val intent = Intent(context, DetailRelay::class.java)
                intent.putExtra("id", list.id)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }

        override fun onClick(v: View?) {
        }

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(List[position], context)
    }
}