package com.reversecurrent.tennistracker.dal.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import com.reversecurrent.tennistracker.dal.SESSION_PLAYER_JOIN_TABLE_NAME
import com.reversecurrent.tennistracker.dal.SESSION_PLAYER_JOIN_TABLE_PLAYER_ID_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_PLAYER_JOIN_TABLE_SESSION_ID_COLUMN

@Entity(
    tableName = SESSION_PLAYER_JOIN_TABLE_NAME,
    primaryKeys = [SESSION_PLAYER_JOIN_TABLE_PLAYER_ID_COLUMN, SESSION_PLAYER_JOIN_TABLE_SESSION_ID_COLUMN],
    foreignKeys = [
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = ["uid"],
            childColumns = [SESSION_PLAYER_JOIN_TABLE_PLAYER_ID_COLUMN]
        ),
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["uid"],
            childColumns = [SESSION_PLAYER_JOIN_TABLE_SESSION_ID_COLUMN]
        )
    ]
)
data class SessionPlayerJoinEntity(
    val playerId: Long,
    val sessionId: Long
)