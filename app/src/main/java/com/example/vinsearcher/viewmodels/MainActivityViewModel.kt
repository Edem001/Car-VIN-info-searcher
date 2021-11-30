package com.example.vinsearcher.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vinsearcher.network.CarImage
import com.example.vinsearcher.network.CarInfoModule
import com.example.vinsearcher.network.models.VehicleModel
import com.example.vinsearcher.room.datamodels.VinEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    val carClient: CarInfoModule,
    val imageClient: CarImage
) : ViewModel() {
    fun query(query: String) {

        CoroutineScope(Dispatchers.IO).launch {
            if (searchHistory.value?.containsKey(query) != true) {
                try {
                    val tempMap = searchHistory.value ?: HashMap()
                    val tempQueryList = queryOrder.value ?: ArrayList()
                    async {
                        searchHistory.postValue(tempMap.apply {
                            put(query, carClient.getInfoByVIN(query))
                        })
                    }.await()

                    val imageList = imageURLList.value ?: ArrayList()
                    imageList.add(Pair(null, false))
                    imageURLList.postValue(imageList)
                    queryOrder.postValue(tempQueryList.apply { add(query) })

                    val vehicleModel = searchHistory.value?.get(query)
                    if (vehicleModel != null) async {
                        val make = vehicleModel.results.find { it.variable == "Make" }?.value
                        val model = vehicleModel.results.find { it.variable == "Model" }?.value

                        val imageSearchQuery = vehicleModel.getImageQuery()
                        var result: String? = null
                        try {
                            result = imageClient.getImage(imageSearchQuery).results[0]
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        imageList.removeLast()
                        imageList.add(Pair(result, true))
                        imageURLList.postValue(imageList)
                    }

                } catch (t: HttpException) {
                    errorObservable.postValue(t.code())
                } catch (e: Exception) {
                    Log.e("Network", e.message.toString())
                    e.printStackTrace()
                }
            }
        }

    }

    val searchHistory = MutableLiveData<HashMap<String, VehicleModel>>()
    val imageURLList = MutableLiveData<ArrayList<Pair<String?, Boolean>>>()
    val queryOrder = MutableLiveData<ArrayList<String>>()
    val errorObservable = MutableLiveData(0)

    val likedButtonState = MutableLiveData(false)


    fun toRoomList(): List<VinEntry> {
        val list = ArrayList<VinEntry>()
        var offset = 0

        if(queryOrder.value != null && queryOrder.value!!.size > 20){
            offset = queryOrder.value!!.size - 20
        }

        queryOrder.value?.forEachIndexed { index, s ->
            if (offset > 0 && index < offset)
                return@forEachIndexed

            list.add(
                VinEntry(
                    s,
                    searchHistory.value?.get(s)!!,
                    imageURLList.value?.get(index)?.first
                )
            )
        }

        return list
    }

    fun parseRoomList(data: List<VinEntry>) {

        val urls = ArrayList<Pair<String?, Boolean>>()
        val queries = ArrayList<String>()
        val vehicles = java.util.HashMap<String, VehicleModel>()

        data.forEachIndexed { index, it ->
            urls.add(
                if (it.imageUrl == null || it.imageUrl == "null") Pair(
                    null,
                    false
                ) else Pair(it.imageUrl, true)
            )
            queries.add(it.vin)
            vehicles.put(it.vin, it.carInfo)
        }

        imageURLList.postValue(urls)
        searchHistory.postValue(vehicles)
        queryOrder.postValue(queries)

        urls.forEachIndexed { index, it ->
            if (it.first == null) {
                CoroutineScope(Dispatchers.IO).launch {
                    urls[index] = Pair(
                        imageClient.getImage(
                            vehicles[queries[index]]
                                ?.getImageQuery() ?: "https://http.cat/404"
                        ).results[0], true
                    ).also { imageURLList.postValue(urls) }
                }
            }
        }
    }
}