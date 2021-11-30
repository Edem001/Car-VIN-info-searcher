package com.example.vinsearcher.recycler_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.postOnAnimationDelayed
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vinsearcher.R
import com.example.vinsearcher.network.models.VehicleModel
import com.example.vinsearcher.util.StringListDiffCallback
import com.example.vinsearcher.util.gone
import com.example.vinsearcher.util.visible

class MainCarAdapter(
    val context: Context,
    var dataListKeys: List<String>,
    var dataList: HashMap<String, VehicleModel>?,
    var urlList: List<Pair<String?, Boolean>>?,
    val itemClickCallback: ItemClickCallback
) : RecyclerView.Adapter<MainCarAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.main_recycler_car_item_header)
        val content = view.findViewById<TextView>(R.id.main_recycler_car_item_content)
        val progress = view.findViewById<ProgressBar>(R.id.main_recycler_progress)
        val deleteButton = view.findViewById<Button>(R.id.main_recycler_car_item_delete_button)
    }

    interface ItemClickCallback {
        fun getItem(vehicleModel: VehicleModel?, imageURL: String?)
        fun onDeleteItem(vehicleModel: VehicleModel?, index: Int)
    }

    val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.main_recycler_car_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val size = dataListKeys.size
        holder.title.text =
            dataList?.get(dataListKeys[size - position - 1])?.searchCriteria ?: "Data error"
        holder.content.text =
            dataList?.get(dataListKeys[size - position - 1])?.getBriefDescription()
                ?: "Looks like data is missing!"

        val isLoaded = urlList?.get(size - position - 1)?.second
        when (isLoaded) {
            false -> holder.progress.visible()
            true -> holder.progress.gone()
        }

        holder.itemView.setOnClickListener {
            itemClickCallback.getItem(
                dataList?.get(dataListKeys[size - position - 1]),
                when (isLoaded) {
                    true -> urlList?.getOrNull(size - position - 1)?.first
                    false -> "https://http.cat/102"
                    null -> "https://http.cat/404"
                }
            )
        }

        holder.deleteButton.setOnClickListener {
            itemClickCallback.onDeleteItem(
                dataList?.get(dataListKeys[size - position - 1]),
                size - position - 1
            )
        }
    }

    override fun getItemCount() = dataListKeys.size
}