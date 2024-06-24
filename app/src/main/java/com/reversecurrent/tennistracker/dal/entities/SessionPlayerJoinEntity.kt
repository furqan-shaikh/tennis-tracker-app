package com.reversecurrent.tennistracker.dal.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import com.reversecurrent.tennistracker.dal.SESSION_PLAYER_JOIN_TABLE_NAME

@Entity(
    tableName = SESSION_PLAYER_JOIN_TABLE_NAME,
    primaryKeys = ["playerId", "sessionId"],
    foreignKeys = [
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = ["uid"],
            childColumns = ["playerId"]
        ),
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["uid"],
            childColumns = ["sessionId"]
        )
    ]
)
data class SessionPlayerJoinEntity(
    val playerId: Long,
    val sessionId: Long
)