package com.reversecurrent.tennistracker.dal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_NAME
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_NAME_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_PLAYER_JOIN_TABLE_NAME
import com.reversecurrent.tennistracker.dal.VENUE_TABLE_NAME_COLUMN
import com.reversecurrent.tennistracker.dal.entities.PlayerEntity
import com.reversecurrent.tennistracker.dal.entities.VenueEntity

@Dao
interface SessionPlayersDao {
    // Get all the players in a session
    @Query("SELECT p.$PLAYER_TABLE_NAME_COLUMN from $PLAYER_TABLE_NAME p " +
            "INNER JOIN $SESSION_PLAYER_JOIN_TABLE_NAME sp ON p.uid = sp.playerId" +
            " WHERE sp.sessionId = :sessionId")
    suspend fun getPlayers(sessionId: Long): List<String>

    @Query("SELECT p.uid from $PLAYER_TABLE_NAME p " +
            "INNER JOIN $SESSION_PLAYER_JOIN_TABLE_NAME sp ON p.uid = sp.playerId" +
            " WHERE sp.sessionId = :sessionId")
    suspend fun getPlayerIds(sessionId: Long): List<Long>

    @Query("DELETE FROM $SESSION_PLAYER_JOIN_TABLE_NAME WHERE playerId = :playerID")
    suspend fun deletePlayer(playerID: Long)

    @Query("SELECT sessionId FROM $SESSION_PLAYER_JOIN_TABLE_NAME WHERE playerId = :playerID")
    suspend fun getSessionsForPlayer(playerID: Long): List<Long>

    @Query("DELETE FROM $SESSION_PLAYER_JOIN_TABLE_NAME WHERE sessionId = :sessionId")
    suspend fun deleteSession(sessionId: Long)
}