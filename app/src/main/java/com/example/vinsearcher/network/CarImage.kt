package com.example.vinsearcher.network

import com.example.vinsearcher.network.models.ImageSearcherModule
import retrofit2.http.GET
import retrofit2.http.Query

interface CarImage {
    @GET("1")
    suspend fun getImage(
        @Query("q") query: String
    ): ImageSearcherModule
}