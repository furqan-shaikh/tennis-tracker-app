package com.reversecurrent.tennistracker.repository

import android.support.annotation.WorkerThread
import com.reversecurrent.tennistracker.dal.database.DatabaseAccessor
import com.reversecurrent.tennistracker.models.PlayerPlayedWith
import com.reversecurrent.tennistracker.models.TennisAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TennisAnalyticsRepository {
    @WorkerThread
    suspend fun getTennisAnalytics(context: Any): TennisAnalytics {
//            totalHoursPlayed: Float,  sum(sessionDuration from sessions table
//            val totalShotsPlayed: Int, sum($SESSION_TABLE_NUMBER_OF_SHOTS_PLAYED_COLUMN) from sessions table
//            val totalStepsTaken: Int, sum($SESSION_TABLE_NUMBER_OF_STEPS_TAKEN_COLUMN) from sessions table

//            val totalSetsPlayed: Int, count of tennis stats table
//            val totalSetsWon: Int     where won by is self
        //            val playedWithTheMost: PlayerPlayedWith,
//            val playedWithTheLeast: PlayerPlayedWith,

        return withContext(Dispatchers.IO) {
            val dbClient = DatabaseAccessor.getDatabase(applicationContext = context)
            val basicStatsFromSessions = dbClient.tennisAnalyticsDao().getBasicStats()
            val tennisAnalytics = TennisAnalytics(
                totalHoursPlayed = basicStatsFromSessions.totalHoursPlayed,
                totalShotsPlayed = basicStatsFromSessions.totalShotsPlayed,
                totalStepsTaken = basicStatsFromSessions.totalStepsTaken,

            )
            return@withContext tennisAnalytics

        }
    }
}