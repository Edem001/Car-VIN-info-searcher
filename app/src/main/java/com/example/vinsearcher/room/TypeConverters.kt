package com.example.vinsearcher.room

import androidx.room.TypeConverter
import com.example.vinsearcher.network.models.VehicleModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverters {
    @TypeConverter
    fun fromListVehiclesToGson(vehicles: List<VehicleModel>): String{
        return Gson().toJson(vehicles)
    }

    @TypeConverter
    fun fromJsonToVehicles(json: String): List<VehicleModel>{
        val typeToken = object:TypeToken<List<VehicleModel>>(){}.type
        return Gson().fromJson(json, typeToken)
    }

    @TypeConverter
    fun fromStringListToJson(data: List<String?>): String{
        return Gson().toJson(data)
    }

    @TypeConverter
    fun fromJsonToStringList(json: String): List<String> {
        val typeToken = object : TypeToken<List<String>>(){}.type
        return Gson().fromJson(json, typeToken)
    }

    @TypeConverter
    fun fromVehicleToJson(vehicle: VehicleModel): String{
        return Gson().toJson(vehicle)
    }

    @TypeConverter
    fun fromJsonToVehicle(json: String): VehicleModel{
        return Gson().fromJson(json, VehicleModel::class.java)
    }
}