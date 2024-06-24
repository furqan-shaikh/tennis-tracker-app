package com.reversecurrent.tennistracker.dal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_PAYMENT_HAS_PAID_COLUMN
import com.reversecurrent.tennistracker.dal.entities.SessionEntity
import com.reversecurrent.tennistracker.dal.entities.SessionPlayerJoinEntity

@Dao
interface SessionDao {
    @Insert
    suspend fun insertSession(session: SessionEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlayerSessionJoin(join: SessionPlayerJoinEntity)

    @Transaction
    suspend fun insertSessionWithPlayers(sessionEntity: SessionEntity, playerIds: List<Long>): Long {
        // Insert the session into the sessions table
        val sessionId = insertSession(sessionEntity)

        // Associate the session with players in the junction table
        playerIds.forEach { playerId ->
            insertPlayerSessionJoin(SessionPlayerJoinEntity(playerId, sessionId))
        }

        return sessionId
    }

    @Query("SELECT * FROM sessions WHERE uid=:uid LIMIT 1")
    suspend fun getById(uid: Long): SessionEntity

    @Query("SELECT * FROM sessions WHERE $SESSION_TABLE_PAYMENT_HAS_PAID_COLUMN = 0")
    suspend fun getAllOutstandingPaymentsForCourts(): List<SessionEntity>
}