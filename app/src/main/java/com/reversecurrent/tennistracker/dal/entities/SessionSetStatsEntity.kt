package com.reversecurrent.tennistracker.dal.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

import com.reversecurrent.tennistracker.dal.SESSION_SET_STATS_TABLE_ACES_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_SET_STATS_TABLE_DOUBLE_FAULTS_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_SET_STATS_TABLE_NAME
import com.reversecurrent.tennistracker.dal.SESSION_SET_STATS_TABLE_SCORE_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_SET_STATS_TABLE_SESSION_ID_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_SET_STATS_TABLE_WINNERS_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_SET_STATS_TABLE_WON_BY_COLUMN

@Entity(tableName = SESSION_SET_STATS_TABLE_NAME,
    foreignKeys = [
        ForeignKey(entity = SessionEntity::class,
        childColumns = [SESSION_SET_STATS_TABLE_SESSION_ID_COLUMN],
        parentColumns = ["uid"]),

        ForeignKey(entity = PlayerEntity::class,
            childColumns = [SESSION_SET_STATS_TABLE_WON_BY_COLUMN],
            parentColumns = ["uid"])
])
data class SessionSetStatsEntity (
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = SESSION_SET_STATS_TABLE_SESSION_ID_COLUMN) val sessionId: Long, // Foreign key column to session
    @ColumnInfo(name = SESSION_SET_STATS_TABLE_WON_BY_COLUMN) val wonBy: Long,  // Foreign key column to player

    @ColumnInfo(name = SESSION_SET_STATS_TABLE_SCORE_COLUMN) val setScore: String,
    @ColumnInfo(name = SESSION_SET_STATS_TABLE_ACES_COLUMN, defaultValue = "0") val setAces: Int = 0,
    @ColumnInfo(name = SESSION_SET_STATS_TABLE_WINNERS_COLUMN, defaultValue = "0") val setWinners: Int = 0,
    @ColumnInfo(name = SESSION_SET_STATS_TABLE_DOUBLE_FAULTS_COLUMN, defaultValue = "0") val setDoubleFaults: Int = 0
)

