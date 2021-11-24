package com.example.vinsearcher.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.vinsearcher.MainActivity
import com.example.vinsearcher.MyApplication
import com.example.vinsearcher.R
import com.example.vinsearcher.network.models.VehicleModel
import com.example.vinsearcher.recycler_adapters.CarInfoStaggeredRecyclerAdapter
import com.example.vinsearcher.util.CarInfoUnit
import com.example.vinsearcher.viewmodels.CarInfoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarInfoFragment(val vehicleModel: VehicleModel?) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_car_info, container, false)

        val progressBar = view.findViewById<ProgressBar>(R.id.car_info_main_progress_bar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.car_info_recycler).apply {
            visibility = View.GONE
            animation = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)
        }

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)

        CoroutineScope(Dispatchers.IO).launch {

            val hashMap = HashMap<String, CarInfoUnit>()
            Log.d("Debug", "${vehicleModel?.getBriefDescription()} is valid - ${vehicleModel?.isValid()}")
            if (vehicleModel != null && vehicleModel.isValid()) {
                vehicleModel.results.filter { it.isNotEmpty() }.forEach {
                    hashMap.apply {
                        put(it.variable!!, CarInfoUnit(it.variable, it.value!!))
                    }
                }
            } else {
                hashMap.apply {
                    put(
                        "car_image",
                        CarInfoUnit(
                            "Error",
                            "No manufacturer/model data provided",
                            "https://http.cat/400"
                        )
                    )
                    put("error_info", CarInfoUnit("Error", "No manufacturer/model data provided"))
                }
            }

            val staggeredRecyclerAdapter = CarInfoStaggeredRecyclerAdapter(requireContext(), hashMap)
            withContext(Dispatchers.Main) {
                recyclerView.adapter = staggeredRecyclerAdapter
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        (activity as MainActivity).simpleShowNavigation()
    }
}