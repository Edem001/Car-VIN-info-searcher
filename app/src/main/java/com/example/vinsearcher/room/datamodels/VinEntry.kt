package com.example.vinsearcher.room.datamodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.vinsearcher.network.models.VehicleModel

@Entity
@TypeConverters(com.example.vinsearcher.room.TypeConverters::class)
data class VinEntry(
    @PrimaryKey val vin: String,
    @ColumnInfo(name = "car_info") val carInfo: VehicleModel,
    @ColumnInfo(name = "image_url") val imageUrl: String?
)