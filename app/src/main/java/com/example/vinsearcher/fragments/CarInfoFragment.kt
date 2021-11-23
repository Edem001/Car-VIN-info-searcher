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
import com.example.vinsearcher.viewmodels.CarInfoViewModel

class CarInfoFragment : Fragment() {

    val viewModel: CarInfoViewModel = CarInfoViewModel()
    private var hideBar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideBar = arguments?.getBoolean("hideBottom") ?: false
        Log.d("Debug", "${arguments?.getBoolean("hideBottom")}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_car_info, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.car_info_recycler).apply {
            visibility = View.GONE
            animation = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)
        }
        val progressBar = view.findViewById<ProgressBar>(R.id.car_info_main_progress_bar).apply {
            visibility = View.VISIBLE
        }

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if(!hideBar)
            (activity as MainActivity).simpleShowNavigation()
    }
}