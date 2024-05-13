package com.reversecurrent.tennistracker.dal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.reversecurrent.tennistracker.dal.VENUE_TABLE_NAME_COLUMN
import com.reversecurrent.tennistracker.dal.entities.VenueEntity

@Dao
interface VenueDao {
    @Upsert
    suspend fun Upsert(venue: VenueEntity)

    @Delete
    fun delete(venue: VenueEntity)

    @Query("SELECT * FROM venues order by $VENUE_TABLE_NAME_COLUMN asc")
    fun getAll(): List<VenueEntity>
}