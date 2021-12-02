package com.example.vinsearcher.recycler_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vinsearcher.R
import com.example.vinsearcher.util.CarInfoUnit
import java.util.*

class CarInfoStaggeredRecyclerAdapter(
    private val context: Context,
    val data: HashMap<String, CarInfoUnit>
) : RecyclerView.Adapter<CarInfoStaggeredRecyclerAdapter.ViewHolder>() {

    val keys: MutableList<String> = data.keys.toMutableList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title = view.findViewById<TextView>(R.id.car_info_title)
        val image = view.findViewById<ImageView>(R.id.car_info_image)
        val description = view.findViewById<TextView>(R.id.car_info_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.fragment_car_info_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.animation =
            AnimationUtils.loadAnimation(context, R.anim.scale_from_0_to_100)


        var currentInfoUnit: CarInfoUnit? = data[keys[position]]

        if (currentInfoUnit?.title != null)
            holder.title.text = currentInfoUnit.title
        else holder.title.visibility = View.GONE
        if (currentInfoUnit?.description != null)
            holder.description.text = currentInfoUnit.description

    }

    override fun getItemCount() = keys.size
}