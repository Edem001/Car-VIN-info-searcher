package com.example.vinsearcher.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinsearcher.R
import com.example.vinsearcher.RecyclerAdapters.MainCarAdapter
import com.example.vinsearcher.ViewModels.MainActivityViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.transition.MaterialContainerTransform

class WelcomeFragment : Fragment() {

    val viewModel = MainActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        val toolBar = view.findViewById<MaterialToolbar>(R.id.main_toolbar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.main_recycler)
        val fab = view.findViewById<FloatingActionButton>(R.id.main_button_search)

        val onMenuClick = androidx.appcompat.widget.Toolbar.OnMenuItemClickListener { item ->
            val reduce = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_to_75)
            val increase = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_to_100)

            if (viewModel.likedButtonState.value == true) {
                item?.setActionView(R.layout.menu_item_action_layout)
                item?.actionView?.startAnimation(reduce)
                item?.actionView?.postDelayed({
                    item.icon =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_like, context?.theme)
                }, resources.getInteger(R.integer.material_motion_duration_short_1).toLong())

                item?.actionView?.startAnimation(increase)
                item?.actionView?.postDelayed({
                    item.actionView = null
                }, resources.getInteger(R.integer.material_motion_duration_short_1).toLong())

                viewModel.likedButtonState.postValue(false)
            } else {
                item?.setActionView(R.layout.menu_item_action_layout)
                item?.actionView?.startAnimation(reduce)
                item?.actionView?.postDelayed({
                    item.icon =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_like_filled, context?.theme)
                }, resources.getInteger(R.integer.material_motion_duration_short_1).toLong())

                item?.actionView?.startAnimation(increase)
                item?.actionView?.postDelayed({
                    item.actionView = null
                }, resources.getInteger(R.integer.material_motion_duration_short_1).toLong())

                viewModel.likedButtonState.postValue(true)
            }
            true
        }

        val recyclerAdapter = MainCarAdapter(requireContext(), listOf(1,2,3,4,5,6,7,8,9,10), object: MainCarAdapter.ItemClickCallback{
            override fun getItem(position: Int) {
                val itemFragment = CarInfoFragment()
                itemFragment.sharedElementEnterTransition =  MaterialContainerTransform().apply {
                    drawingViewId = R.id.main_layout
                    duration = resources.getInteger(R.integer.material_motion_duration_medium_1).toLong()
                    scrimColor = Color.TRANSPARENT
                }

                parentFragmentManager.commit {
                    addToBackStack("main")
                    replace(R.id.main_fragment_container, itemFragment)
                }
            }
        })

        toolBar.setOnMenuItemClickListener(onMenuClick)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

}