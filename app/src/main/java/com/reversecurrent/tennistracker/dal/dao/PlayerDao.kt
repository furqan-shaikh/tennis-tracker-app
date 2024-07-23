package com.reversecurrent.tennistracker.dal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_NAME
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_NAME_COLUMN
import com.reversecurrent.tennistracker.dal.entities.PlayerEntity

@Dao
interface PlayerDao {
    @Upsert
    suspend fun upsert(player: PlayerEntity)

    @Query("DELETE FROM $PLAYER_TABLE_NAME WHERE uid =:playerId")
    suspend fun deleteByPlayerId(playerId: Long)

    @Query("SELECT * FROM players WHERE player_name LIKE :playerName LIMIT 10")
    fun findByName(playerName: String): PlayerEntity

    @Query("SELECT * FROM players order by $PLAYER_TABLE_NAME_COLUMN asc")
    fun getAll(): List<PlayerEntity>

    @Query("SELECT * FROM players WHERE uid=:uid")
    suspend fun getById(uid: Long): PlayerEntity

    @Query("SELECT EXISTS(SELECT 1 FROM $PLAYER_TABLE_NAME WHERE $PLAYER_TABLE_NAME_COLUMN = :name)")
    suspend fun selfPlayerExists(name: String): Boolean

    @Query("SELECT * FROM $PLAYER_TABLE_NAME WHERE $PLAYER_TABLE_NAME_COLUMN = :name")
    suspend fun getPlayerByName(name: String): PlayerEntity

}