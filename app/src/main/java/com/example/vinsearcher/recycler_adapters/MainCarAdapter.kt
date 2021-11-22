package com.example.vinsearcher.recycler_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vinsearcher.R

class MainCarAdapter(
    context: Context,
    val dataList: List<Int>,
    val itemClickCallback: ItemClickCallback
) : RecyclerView.Adapter<MainCarAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.main_recycler_car_item_header)
        val content = view.findViewById<TextView>(R.id.main_recycler_car_item_content)
    }

    interface ItemClickCallback {
        fun getItem(position: Int, view: View)
    }

    val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.main_recycler_car_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickCallback.getItem(position, it)
        }
    }

    override fun getItemCount() = dataList.size
}