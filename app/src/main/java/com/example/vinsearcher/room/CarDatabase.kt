package com.example.vinsearcher.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vinsearcher.room.datamodels.VinEntry

@Database(entities = [VinEntry::class], version = 1)
abstract class CarDatabase: RoomDatabase() {
    abstract fun carDAO(): CarDAO
}