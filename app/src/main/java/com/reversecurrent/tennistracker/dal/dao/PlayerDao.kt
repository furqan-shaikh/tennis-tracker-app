package com.reversecurrent.tennistracker.dal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_NAME_COLUMN
import com.reversecurrent.tennistracker.dal.entities.PlayerEntity

@Dao
interface PlayerDao {
    @Upsert
    suspend fun Upsert(player: PlayerEntity)

    @Delete
    fun delete(player: PlayerEntity)

    @Query("SELECT * FROM players WHERE player_name LIKE :player_name LIMIT 10")
    fun findByName(player_name: String): PlayerEntity

    @Query("SELECT * FROM players order by $PLAYER_TABLE_NAME_COLUMN asc")
    fun getAll(): List<PlayerEntity>

    @Query("SELECT * FROM players WHERE uid=:uid")
    suspend fun getById(uid: Long): PlayerEntity
}