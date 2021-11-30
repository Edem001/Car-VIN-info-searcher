package com.example.vinsearcher.room

import androidx.room.*
import com.example.vinsearcher.room.datamodels.VinEntry

@Dao
interface CarDAO {

    @Query("SELECT * from VinEntry")
    fun loadAllEntries(): List<VinEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEntries(entries: List<VinEntry>)

    @Query(value = "DELETE FROM VinEntry WHERE vin = :VIN")
    fun deleteByVIN(VIN : String)

    @Query(value = "DELETE FROM VinEntry")
    fun clearTable()
}