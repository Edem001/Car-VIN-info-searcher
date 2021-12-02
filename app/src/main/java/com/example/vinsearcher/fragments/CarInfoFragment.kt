package com.example.vinsearcher.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.vinsearcher.MyApplication
import com.example.vinsearcher.R
import com.example.vinsearcher.network.CarImage
import com.example.vinsearcher.network.models.VehicleModel
import com.example.vinsearcher.recycler_adapters.CarInfoStaggeredRecyclerAdapter
import com.example.vinsearcher.util.CarInfoUnit
import com.example.vinsearcher.util.gone
import com.example.vinsearcher.util.visible
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class CarInfoFragment : Fragment() {

    var vehicleModel: VehicleModel? = null
    var carImageURL: String? = null

    @Named("Image search")
    @Inject
    lateinit var client: CarImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vehicleModel =
            Gson().fromJson(requireArguments().getString("car"), VehicleModel::class.java)
        carImageURL = requireArguments().getString("image")

        (activity?.application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_car_info, container, false)

        val progressBar = view.findViewById<ProgressBar>(R.id.car_info_main_progress_bar)
        val carImage = view.findViewById<ImageView>(R.id.car_info_image)
        val recyclerView = view.findViewById<RecyclerView>(R.id.car_info_recycler).apply {
            visibility = View.GONE
            animation = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)
        }
        val toolbar = view.findViewById<AppBarLayout>(R.id.car_info_app_bar).apply { gone() }
        val pinnedToolbar = view.findViewById<Toolbar>(R.id.car_info_toolbar).apply {
            gone()
            val make = vehicleModel?.results?.find { it.variable == "Make" }?.value
            val model = vehicleModel?.results?.find { it.variable == "Model" }?.value
            if (!(make == null && model == null))
                title = "${make.let { make } ?: ""} ${model.let { model } ?: ""}"
        }

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)

        CoroutineScope(Dispatchers.IO).launch {

            val hashMap = HashMap<String, CarInfoUnit>()
            if (vehicleModel != null && vehicleModel!!.isValid()) {
                setImageHolder(
                    carImage,
                    carImageURL ?: "https://http.cat/404"
                )

                vehicleModel!!.results.filter { it.isNotEmpty() }.forEach {
                    hashMap.apply {
                        put(it.variable!!, CarInfoUnit(it.variable, it.value!!))
                    }
                }
            } else {

                setImageHolder(
                    carImage,
                    "https://http.cat/400"
                )
                hashMap.apply {
                    put("error_info", CarInfoUnit("Error", "No manufacturer/model data provided"))
                }
            }

            val staggeredRecyclerAdapter =
                CarInfoStaggeredRecyclerAdapter(requireContext(), hashMap)
            withContext(Dispatchers.Main) {
                recyclerView.adapter = staggeredRecyclerAdapter
                progressBar.gone()
                toolbar.visible()
                pinnedToolbar.visible()
                recyclerView.visible()
            }
        }

        return view
    }


    private suspend fun setImageHolder(imageHolder: ImageView, url: String) {
        withContext(Dispatchers.Main) {
            val imageLoadingCallback = object : Callback {
                override fun onSuccess() {
                    imageHolder.visibility = View.VISIBLE
                }

                override fun onError(e: Exception?) {

                }
            }

            Picasso.get()
                .load(url)
                .placeholder(R.drawable.surface_pad)
                .error(R.drawable.ic_baseline_error_24)
                .into(imageHolder, imageLoadingCallback)
        }
    }
}