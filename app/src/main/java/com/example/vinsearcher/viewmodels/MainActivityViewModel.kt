package com.example.vinsearcher.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vinsearcher.network.CarImage
import com.example.vinsearcher.network.CarInfoModule
import com.example.vinsearcher.network.models.VehicleModel
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
                    if (vehicleModel != null) async{
                        val make = vehicleModel.results.find { it.variable == "Make" }?.value
                        val model = vehicleModel.results.find { it.variable == "Model" }?.value

                        var imageSearchQuery: String? = null
                        let {
                            imageSearchQuery =
                                "$make $model ${vehicleModel.results.find { it.variable == "Series" }?.value ?: ""} ${vehicleModel.results.find { it.variable == "Model Year" }?.value ?: ""}"
                        }

                        var result: String? = null
                        try {
                            if (imageSearchQuery != null)
                                result = imageClient.getImage(imageSearchQuery!!).results[0]
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        imageList.removeLast()
                        imageList.add(Pair(result, true))
                        imageURLList.postValue(imageList)
                    }

                } catch (t: HttpException) {
                    errorObservable.postValue(t.code())
                }
            }
        }

    }

    val searchHistory = MutableLiveData<HashMap<String, VehicleModel>>()
    val imageURLList = MutableLiveData<ArrayList<Pair<String?, Boolean>>>()
    val queryOrder = MutableLiveData<ArrayList<String>>()
    val errorObservable = MutableLiveData(0)

    val likedButtonState = MutableLiveData(false)
}