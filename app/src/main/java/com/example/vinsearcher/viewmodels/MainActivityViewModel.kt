package com.example.vinsearcher.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vinsearcher.network.CarInfoModule
import com.example.vinsearcher.network.models.VehicleModel
import com.example.vinsearcher.util.CarInfoUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.reflect.typeOf

class MainActivityViewModel @Inject constructor(val carClient: CarInfoModule) : ViewModel() {
    fun query(query: String) {

        CoroutineScope(Dispatchers.IO).launch {
            if (searchHistory.value?.containsKey(query) != true) {
                try {
                    val tempMap = searchHistory.value ?: HashMap()
                    val tempQueryList = queryOrder.value ?: ArrayList()
                    searchHistory.postValue(tempMap.apply {
                        put(query, carClient.getInfoByVIN(query))
                    })

                    queryOrder.postValue(tempQueryList.apply { add(query) })
                } catch (t: HttpException) {
                    errorObservable.postValue(t.code())
                }
            }
        }

    }

    val searchHistory = MutableLiveData<HashMap<String, VehicleModel>>()
    val queryOrder = MutableLiveData<ArrayList<String>>()
    val errorObservable = MutableLiveData(0)

    val likedButtonState = MutableLiveData(false)
}