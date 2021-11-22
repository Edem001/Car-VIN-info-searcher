package com.example.vinsearcher.fragments

import android.os.Build
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
import com.example.vinsearcher.recycler_adapters.MainCarAdapter
import com.example.vinsearcher.viewmodels.MainActivityViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
            val reduce = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
            val increase = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)

            if (viewModel.likedButtonState.value == true) {
                item?.icon = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_time_icon,
                    requireContext().theme
                )
                item?.title = resources.getString(R.string.menu_item_history)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    item?.setContentDescription(resources.getString(R.string.menu_item_history_description))
                }

                viewModel.likedButtonState.postValue(false)
            } else {
                item?.icon = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_like_filled,
                    requireContext().theme
                )
                item?.title = resources.getString(R.string.menu_item_liked)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    item?.setContentDescription(resources.getString(R.string.menu_item_history_description))
                }

                viewModel.likedButtonState.postValue(true)
            }
            true
        }

        val recyclerAdapter = MainCarAdapter(
            requireContext(),
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            object : MainCarAdapter.ItemClickCallback {
                override fun getItem(position: Int, innerView: View) {
                    parentFragmentManager.commit {
                        setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            android.R.anim.fade_in,
                            R.anim.slide_out
                        )
                        replace(R.id.main_fragment_container, CarInfoFragment())
                        addToBackStack("main")
                    }
                }
            })

        toolBar.setOnMenuItemClickListener(onMenuClick)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

}