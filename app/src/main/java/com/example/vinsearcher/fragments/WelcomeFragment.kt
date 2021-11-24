package com.example.vinsearcher.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.animation.addListener
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinsearcher.MainActivity
import com.example.vinsearcher.MyApplication
import com.example.vinsearcher.R
import com.example.vinsearcher.network.models.VehicleModel
import com.example.vinsearcher.recycler_adapters.MainCarAdapter
import com.example.vinsearcher.viewmodels.MainActivityViewModel
import com.google.android.material.animation.AnimatorSetCompat.playTogether
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

class WelcomeFragment : Fragment() {

    @Inject
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        val toolBar = view.findViewById<MaterialToolbar>(R.id.main_toolbar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.main_recycler)

        val fragmentContainer =
            view.findViewById<FragmentContainerView>(R.id.welcome_fragment_container)

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
            listOf(),
            null,
            object : MainCarAdapter.ItemClickCallback {
                override fun getItem(vehicleModel: VehicleModel?) {
                    parentFragmentManager.commit {
                        setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            android.R.anim.fade_in,
                            R.anim.slide_out
                        )
                        replace(R.id.main_fragment_container, CarInfoFragment(vehicleModel))
                        (activity as MainActivity).simpleHideNavigation()
                        addToBackStack("main")
                    }
                }
            })

        toolBar.setOnMenuItemClickListener(onMenuClick)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val observer = Observer<ArrayList<String>> {
            recyclerAdapter.dataListKeys = it
            recyclerAdapter.dataList = viewModel.searchHistory.value
            recyclerAdapter.notifyDataSetChanged()
        }

        viewModel.queryOrder.observe(viewLifecycleOwner, observer)

        return view
    }

}