package com.example.vinsearcher.network.models

import com.google.gson.annotations.SerializedName

data class VehicleResultsItemModel(
    @SerializedName("Value") val value: String,
    @SerializedName("ValueId") val valueId: String,
    @SerializedName("Variable") val variable: String,
    @SerializedName("VariableId") val variableId: Int
)