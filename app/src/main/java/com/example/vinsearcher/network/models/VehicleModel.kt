package com.example.vinsearcher.network.models

import com.google.gson.annotations.SerializedName

data class VehicleModel(
    @SerializedName("Count") val count: Int,
    @SerializedName("Message") val message: String,
    @SerializedName("SearchCriteria") val searchCriteria: String,
    @SerializedName("Results") val results: List<VehicleResultsItemModel>
)