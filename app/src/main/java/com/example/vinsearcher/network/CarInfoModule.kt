package com.example.vinsearcher.network

import com.example.vinsearcher.network.models.VehicleModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CarInfoModule {

    @GET("vehicles/DecodeVin/{vin}")
    suspend fun getInfoByVIN(
        @Path("vin") path: String,
        @Query("format") format: String = "json",
        @Query("modelyear") modelYear: Int? = null
    ): VehicleModel
}