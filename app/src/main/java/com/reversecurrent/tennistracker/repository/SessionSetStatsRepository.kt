package com.reversecurrent.tennistracker.repository

import android.support.annotation.WorkerThread
import android.util.Log
import androidx.room.withTransaction
import com.reversecurrent.tennistracker.dal.database.DatabaseAccessor
import com.reversecurrent.tennistracker.dal.entities.SessionSetStatsEntity
import com.reversecurrent.tennistracker.models.SetStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SessionSetStatsRepository {
    @WorkerThread
    suspend fun upsertBatch(context: Any, sessionSetStatsList: List<SetStats>): List<Long> {
        val ids = mutableListOf<Long>()
        return withContext(Dispatchers.IO) {
            try{
                val dbClient = DatabaseAccessor.getDatabase(applicationContext = context)

                dbClient.withTransaction {
                    sessionSetStatsList.forEach {
                        ids.add(upsert(context, it))
                    }
                }
                return@withContext ids
            }
            catch (e: Exception){
                Log.e("SessionSetStatsRepository", e.toString())
                return@withContext ids
            }
        }
    }
    @WorkerThread
    suspend fun upsert(context: Any, sessionSetStats: SetStats): Long {
        return withContext(Dispatchers.IO) {
            val sessionSetStatsEntity = SessionSetStatsEntity(
                sessionId = sessionSetStats.sessionId,
                wonBy = sessionSetStats.playerId,
                setScore = sessionSetStats.setScore,
                setWinners = sessionSetStats.setWinners,
                setAces = sessionSetStats.setAces,
                setDoubleFaults = sessionSetStats.setDoubleFaults,
                uid = 0
            )
            val id = DatabaseAccessor.getDatabase(context).sessionSetStatsDao().upsertSessionSetStats(sessionSetStatsEntity)
            return@withContext id
        }
    }
}