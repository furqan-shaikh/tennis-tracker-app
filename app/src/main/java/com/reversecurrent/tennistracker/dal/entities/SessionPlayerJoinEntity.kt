package com.reversecurrent.tennistracker.dal.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "player_session_join",
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