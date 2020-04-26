package com.example.whitecube.GantiDevice

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whitecube.R
import kotlinx.android.synthetic.main.item_ganti_device.view.*

class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    val name = itemView.namaDevice
    val ceklis = itemView.ceklis


    fun bind(device: PilihDevice,clickListener: OnItemClickListener)
    {
        name.text = device.nama
        if(device.status=="1"){
            ceklis.visibility = View.VISIBLE
        }

        itemView.setOnClickListener {
            clickListener.onItemClicked(device)
        }
    }

}


class RecyclerAdapter(var devices:MutableList<PilihDevice>, val itemClickListener: OnItemClickListener):RecyclerView.Adapter<MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ganti_device,parent,false)
        return MyHolder(view)


    }

    override fun getItemCount(): Int {
        return devices.size
    }

    override fun onBindViewHolder(myHolder: MyHolder, position: Int) {
        val user = devices.get(position)
        myHolder.bind(user,itemClickListener)

    }
}


interface OnItemClickListener{
    fun onItemClicked(device: PilihDevice)
}