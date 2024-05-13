package com.reversecurrent.tennistracker.dal.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_MOBILE_NUMBER_COLUMN
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_NAME
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_NAME_COLUMN
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_PLAYED_BEFORE_COLUMN
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_PLAYING_LEVEL_COLUMN

@Entity(tableName = PLAYER_TABLE_NAME)
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name=PLAYER_TABLE_NAME_COLUMN) val playerName: String,
    @ColumnInfo(name=PLAYER_TABLE_MOBILE_NUMBER_COLUMN) val mobileNumber: String,
    @ColumnInfo(name=PLAYER_TABLE_PLAYING_LEVEL_COLUMN) val playingLevel: String = "Beginner",
    @ColumnInfo(name=PLAYER_TABLE_PLAYED_BEFORE_COLUMN) val playedBefore: Boolean = true
)
