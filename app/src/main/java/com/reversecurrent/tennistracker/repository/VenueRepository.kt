package com.reversecurrent.tennistracker.repository

import android.support.annotation.WorkerThread
import android.util.Log
import androidx.compose.runtime.traceEventEnd
import com.reversecurrent.tennistracker.dal.entities.PlayerEntity
import com.reversecurrent.tennistracker.models.Player
import com.reversecurrent.tennistracker.dal.database.DatabaseAccessor
import com.reversecurrent.tennistracker.dal.entities.VenueEntity
import com.reversecurrent.tennistracker.models.Venue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VenueRepository {
    @WorkerThread
    suspend fun upsert(context: Any, venue: Venue) {
        val venueEntity = VenueEntity(
            venueName = venue.venueName,
            venueAlias = venue.venueAlias,
            address = venue.address,
            costPerHour = venue.costPerHour,
            courtType = venue.courtType,
            coachingAvailable = venue.coachingAvailable,
            numberOfCourts = venue.numberOfCourts,
            primaryContactName = venue.primaryContactName,
            primaryContactNumber = venue.primaryContactNumber,
            uid = 1
        )
        Log.i("VenueRepository", venueEntity.toString())
        Log.i("VenueRepository", "Before upserting venue")
        DatabaseAccessor.getDatabase(applicationContext = context)
            .venueDao().Upsert(venueEntity)
    }

    @WorkerThread
    suspend fun getAllVenues(context: Any): List<Venue> {
        return withContext(Dispatchers.IO) {
            val venueEntities = DatabaseAccessor.getDatabase(applicationContext = context)
                .venueDao().getAll()

            val venues = venueEntities.map { venueEntity ->
                Venue(
                    venueId = venueEntity.uid,
                    venueName = venueEntity.venueName,
                    venueAlias = venueEntity.venueAlias,
                    address = venueEntity.address,
                    costPerHour = venueEntity.costPerHour,
                    courtType = venueEntity.courtType,
                    coachingAvailable = venueEntity.coachingAvailable,
                    numberOfCourts = venueEntity.numberOfCourts,
                    primaryContactName = venueEntity.primaryContactName,
                    primaryContactNumber = venueEntity.primaryContactNumber
                )
            }
            return@withContext venues
        }

    }
}