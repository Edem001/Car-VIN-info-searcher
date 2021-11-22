package com.example.vinsearcher.recycler_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vinsearcher.R
import com.example.vinsearcher.util.CarInfoUnit
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.*

class CarInfoStaggeredRecyclerAdapter(
    private val context: Context,
    val data: Hashtable<String, CarInfoUnit>
) : RecyclerView.Adapter<CarInfoStaggeredRecyclerAdapter.ViewHolder>() {

    val keys: MutableList<String> = data.keys.toMutableList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title = view.findViewById<TextView>(R.id.car_info_title)
        val image = view.findViewById<ImageView>(R.id.car_info_image)
        val description = view.findViewById<TextView>(R.id.car_info_description)
        val progressBar = view.findViewById<ProgressBar>(R.id.car_info_progress_bar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_car_info_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.animation =
            AnimationUtils.loadAnimation(context, R.anim.scale_from_0_to_100)
        val currentInfoUnit: CarInfoUnit?
        val imageLoadingCallback = object : Callback {
            override fun onSuccess() {
                holder.progressBar.visibility = View.GONE
            }

            override fun onError(e: Exception?) {

            }
        }

        if (position == 0 && keys.contains("car_image")) {

            val imagePosition = keys.indexOf("car_image")
            val tempData = keys[0]
            keys[0] = keys[imagePosition]
            keys[imagePosition] = tempData

            currentInfoUnit = data[keys[position]]
            holder.title.visibility = View.GONE
            holder.description.visibility = View.GONE
            holder.image.visibility = View.VISIBLE
            holder.progressBar.visibility = View.VISIBLE

            Picasso.get()
                .load(currentInfoUnit?.imageURL)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_like_filled)
                .into(holder.image, imageLoadingCallback)
            return
        }

        currentInfoUnit = data[keys[position]]
        if (currentInfoUnit?.imageURL != null){
            holder.image.visibility = View.VISIBLE
            holder.progressBar.visibility = View.VISIBLE

            Picasso.get()
                .load(currentInfoUnit.imageURL)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_like_filled)
                .into(holder.image, imageLoadingCallback)
        }

        holder.title.text = currentInfoUnit?.title
        holder.description.text = currentInfoUnit?.description
    }

    override fun getItemCount() = data.size
}