package com.reversecurrent.tennistracker.dal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_HAS_PAID_TO_COURT
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_NAME
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_PAYMENT_BOOKED_BY
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_PAYMENT_HAS_PAID_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_SESSION_DATE_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_WASHED_OUT_COLUMN
import com.reversecurrent.tennistracker.dal.entities.SessionEntity
import com.reversecurrent.tennistracker.dal.entities.SessionPlayerJoinEntity
import com.reversecurrent.tennistracker.models.SELF_PLAYER

@Dao
interface SessionDao {
    @Upsert
    suspend fun insertSession(session: SessionEntity): Long

    @Upsert
    suspend fun insertPlayerSessionJoin(join: SessionPlayerJoinEntity)

    @Transaction
    suspend fun insertSessionWithPlayers(sessionEntity: SessionEntity, playerIds: List<Long>): Long {
        // Insert the session into the sessions table
        var sessionId = insertSession(sessionEntity)
        // When its an upsert, room returns -1 for session id, so we reset it to the actual session id
        if (sessionId == -1L) {
            sessionId = sessionEntity.uid
        }

        // Associate the session with players in the junction table
        playerIds.forEach { playerId ->
            insertPlayerSessionJoin(SessionPlayerJoinEntity(playerId, sessionId))
        }

        return sessionId
    }

    @Query("SELECT * FROM $SESSION_TABLE_NAME WHERE uid=:uid LIMIT 1")
    suspend fun getById(uid: Long): SessionEntity

    @Query("SELECT * FROM $SESSION_TABLE_NAME WHERE $SESSION_TABLE_HAS_PAID_TO_COURT = 0 AND $SESSION_TABLE_WASHED_OUT_COLUMN = \"0\" AND $SESSION_TABLE_PAYMENT_BOOKED_BY = \"$SELF_PLAYER\"")
    suspend fun getAllOutstandingPaymentsForCourts(): List<SessionEntity>

    @Query("SELECT * FROM $SESSION_TABLE_NAME WHERE $SESSION_TABLE_PAYMENT_HAS_PAID_COLUMN = 0 AND $SESSION_TABLE_PAYMENT_BOOKED_BY != \"$SELF_PLAYER\" AND $SESSION_TABLE_WASHED_OUT_COLUMN = \"0\"")
    suspend fun getAllOutstandingPaymentsBySelf(): List<SessionEntity>

    @Query("SELECT * FROM $SESSION_TABLE_NAME order by $SESSION_TABLE_SESSION_DATE_COLUMN desc")
    fun getAll(): List<SessionEntity>

    @Query("DELETE FROM $SESSION_TABLE_NAME WHERE uid=:sessionId")
    suspend fun deleteBySessionId(sessionId: Long)
}