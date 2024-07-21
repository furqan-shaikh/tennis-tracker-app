package com.reversecurrent.tennistracker.dal.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.reversecurrent.tennistracker.dal.SESSION_SET_STATS_TABLE_NAME
import com.reversecurrent.tennistracker.dal.SESSION_SET_STATS_TABLE_SESSION_ID_COLUMN

import com.reversecurrent.tennistracker.dal.entities.SessionSetStatsEntity

@Dao
interface SessionSetStatsDao {
    @Upsert
    suspend fun upsertSessionSetStats(sessionSetStatsEntity: SessionSetStatsEntity): Long

    @Query("SELECT * FROM $SESSION_SET_STATS_TABLE_NAME WHERE $SESSION_SET_STATS_TABLE_SESSION_ID_COLUMN = :sessionId")
    suspend fun getSessionSetStats(sessionId: Long): List<SessionSetStatsEntity>

    @Query("DELETE FROM $SESSION_SET_STATS_TABLE_NAME WHERE $SESSION_SET_STATS_TABLE_SESSION_ID_COLUMN = :sessionId")
    suspend fun deleteSessionSetStats(sessionId: Long)
}