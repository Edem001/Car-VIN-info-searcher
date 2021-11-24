package com.example.vinsearcher.network.models

import android.util.Log
import com.google.gson.annotations.SerializedName

data class VehicleModel(
    @SerializedName("Count") val count: Int,
    @SerializedName("Message") val message: String,
    @SerializedName("SearchCriteria") val searchCriteria: String,
    @SerializedName("Results") val results: List<VehicleResultsItemModel>
) {
    fun getBriefDescription(): String {
        val items = results.filter { it.variable in listOf("Manufacturer Name", "Model") }
        return "Manufacturer: ${
            items.filter { it.variable == "Manufacturer Name" }.get(0).value ?: "Not found"
        }\n" +
                "Model: ${items.filter { it.variable == "Model" }.get(0).value ?: "Not found"}"
    }

    fun isValid(): Boolean {
        results.filter { it.variable in listOf("Manufacturer Name", "Model") }
            .filterNot { it.value == null }
            .ifEmpty { return false }

        return true
    }
}