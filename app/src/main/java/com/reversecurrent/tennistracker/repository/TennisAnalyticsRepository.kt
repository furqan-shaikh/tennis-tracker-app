package com.reversecurrent.tennistracker.repository

import android.support.annotation.WorkerThread
import com.reversecurrent.tennistracker.dal.database.DatabaseAccessor
import com.reversecurrent.tennistracker.models.SELF_PLAYER
import com.reversecurrent.tennistracker.models.TennisAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TennisAnalyticsRepository {
    @WorkerThread
    suspend fun getTennisAnalytics(context: Any): TennisAnalytics {
        //            val playedWithTheMost: PlayerPlayedWith,
//            val playedWithTheLeast: PlayerPlayedWith,

        return withContext(Dispatchers.IO) {
            val dbClient = DatabaseAccessor.getDatabase(applicationContext = context)
            val basicStatsFromSessions = dbClient.tennisAnalyticsDao().getBasicStats()
            val totalSetsPlayed = dbClient.sessionSetStatsDao().getTotalSetsPlayed()

            // get player id for a player name
            val player = dbClient.playerDao().getPlayerByName(SELF_PLAYER)
            val totalSetsWonBySelf = dbClient.sessionSetStatsDao().getSetsWonBy(player.uid)
            // win percentage
            val setWinsPercentage = if (totalSetsPlayed > 0) {
                (totalSetsWonBySelf * 100) / totalSetsPlayed
            } else {
                0f
            }
            val tennisAnalytics = TennisAnalytics(
                totalHoursPlayed = basicStatsFromSessions.totalHoursPlayed,
                totalShotsPlayed = basicStatsFromSessions.totalShotsPlayed,
                totalStepsTaken = basicStatsFromSessions.totalStepsTaken,
                totalSetsPlayed = totalSetsPlayed,
                totalSetsWonBySelf = totalSetsWonBySelf,
                setWinsPercentage = setWinsPercentage
            )
            return@withContext tennisAnalytics

        }
    }
}