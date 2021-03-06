package com.example.vinsearcher.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinsearcher.MainActivity
import com.example.vinsearcher.MyApplication
import com.example.vinsearcher.R
import com.example.vinsearcher.network.models.VehicleModel
import com.example.vinsearcher.recycler_adapters.MainCarAdapter
import com.example.vinsearcher.room.CarDatabase
import com.example.vinsearcher.util.StringListDiffCallback
import com.example.vinsearcher.viewmodels.MainActivityViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class WelcomeFragment : Fragment() {

    @Inject
    lateinit var viewModel: MainActivityViewModel

    @Inject
    lateinit var room: CarDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        (activity as MainActivity).simpleShowNavigation()

        val toolBar = view.findViewById<MaterialToolbar>(R.id.main_toolbar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.main_recycler)

        val fragmentContainer =
            view.findViewById<FragmentContainerView>(R.id.welcome_fragment_container)

        val recyclerAdapter = MainCarAdapter(
            requireContext(),
            listOf(),
            null,
            listOf(),
            object : MainCarAdapter.ItemClickCallback {
                override fun getItem(vehicleModel: VehicleModel?, imageURL: String?) {
                    parentFragmentManager.commit {
                        setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            android.R.anim.fade_in,
                            R.anim.slide_out
                        )

                        val bundle = Bundle().apply {
                            putString("image", imageURL)
                            putString("car", Gson().toJson(vehicleModel))
                        }
                        replace(
                            R.id.main_fragment_container,
                            CarInfoFragment::class.java,
                            bundle
                        )

                        addToBackStack("main")
                    }
                }

                override fun onDeleteItem(vehicleModel: VehicleModel?, index: Int) {
                    if (vehicleModel != null) {
                        val vin = vehicleModel.searchCriteria.replace("VIN:", "")
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                room.carDAO().deleteByVIN(vin)
                            } catch (e: Exception) {
                                Log.e("Database", e.message.toString())
                                e.printStackTrace()
                            }
                        }

                        val queries = viewModel.queryOrder.value?.toMutableList()
                        val data = viewModel.searchHistory.value?.toMutableMap()
                        val urls = viewModel.imageURLList.value?.toMutableList()

                        urls?.removeAt(index)
                        data?.remove(vin)
                        queries?.removeAt(index)

                        viewModel.searchHistory.postValue(data as HashMap<String, VehicleModel>?)
                        viewModel.imageURLList.postValue(urls as ArrayList<Pair<String?, Boolean>>?)
                        viewModel.queryOrder.postValue(queries as ArrayList<String>?)
                    }

                }
            })

        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        val observer = Observer<List<String>> {
            recyclerAdapter.urlList = viewModel.imageURLList.value
            recyclerAdapter.dataList = viewModel.searchHistory.value

            val diff = StringListDiffCallback(viewModel.queryOrderOld.reversed(), it.reversed())
            val result = DiffUtil.calculateDiff(diff)

            recyclerAdapter.dataListKeys = it
            result.dispatchUpdatesTo(recyclerAdapter)

            if (viewModel.queryOrderOld.size != it.size)
                viewModel.queryOrderOld = it.toList()
        }

        viewModel.queryOrder.observe(viewLifecycleOwner, observer)
        viewModel.loadedIndex.observe(viewLifecycleOwner, {
            if (it >= 0 && recyclerAdapter.itemCount > 0)
                recyclerAdapter.notifyItemChanged(recyclerAdapter.itemCount - it - 1)
            else recyclerAdapter.notifyDataSetChanged()
        })

        return view
    }

    override fun onDestroyView() {
        (activity as MainActivity).simpleHideNavigation()
        super.onDestroyView()
    }
}