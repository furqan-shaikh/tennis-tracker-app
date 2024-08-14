package com.reversecurrent.tennistracker.repository

import android.support.annotation.WorkerThread
import android.util.Log
import com.reversecurrent.tennistracker.dal.database.DatabaseAccessor
import com.reversecurrent.tennistracker.dal.entities.PlayerPlayedWithEntity
import com.reversecurrent.tennistracker.models.PlayerPlayedWith
import com.reversecurrent.tennistracker.models.SELF_PLAYER
import com.reversecurrent.tennistracker.models.TennisAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TennisAnalyticsRepository {
    @WorkerThread
    suspend fun getTennisAnalytics(context: Any): TennisAnalytics {
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
            // most played with and least played with
            val listOfPlayersPlayedWithEntity = dbClient.tennisAnalyticsDao().getPlayedWithStats()
            Log.i("listOfPlayersPlayedWithEntity", listOfPlayersPlayedWithEntity.toString())

            val (mostPlayedWith, leastPlayedWith) = listOfPlayersPlayedWithEntity.takeIf { it.isNotEmpty() }
                ?.let { players ->
                    getPlayerPlayedWith(playerPlayedWithEntity = players.first()) to
                            getPlayerPlayedWith(playerPlayedWithEntity = players.last())
                } ?: (PlayerPlayedWith() to PlayerPlayedWith())

            val tennisAnalytics = TennisAnalytics(
                totalHoursPlayed = basicStatsFromSessions.totalHoursPlayed,
                totalShotsPlayed = basicStatsFromSessions.totalShotsPlayed,
                totalStepsTaken = basicStatsFromSessions.totalStepsTaken,
                totalSetsPlayed = totalSetsPlayed,
                totalSetsWonBySelf = totalSetsWonBySelf,
                setWinsPercentage = setWinsPercentage,
                playedWithTheMost = mostPlayedWith,
                playedWithTheLeast = leastPlayedWith
            )
            return@withContext tennisAnalytics
        }
    }

    private fun getPlayerPlayedWith(playerPlayedWithEntity: PlayerPlayedWithEntity): PlayerPlayedWith {
        return PlayerPlayedWith (
            playerId = playerPlayedWithEntity.playerId,
            playerName = playerPlayedWithEntity.playerName,
            numberOfTimesPlayed = playerPlayedWithEntity.numberOfTimesPlayed,
            totalHoursPlayed = playerPlayedWithEntity.totalHoursPlayed
        )
    }
}