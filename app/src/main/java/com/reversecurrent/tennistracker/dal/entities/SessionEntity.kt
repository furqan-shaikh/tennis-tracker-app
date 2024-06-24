package com.reversecurrent.tennistracker.dal.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_COST_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_DURATION_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_NAME
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_NOTES_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_NUMBER_OF_SHOTS_PLAYED_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_NUMBER_OF_STEPS_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_PAYMENT_AMOUNT_DUE_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_PAYMENT_HAS_PAID_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_PAYMENT_PAID_DATE_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_PLAYING_FORMAT_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_PLAYING_STRUCTURE_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_QUALITY_OF_TENNIS_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_REACHED_TIME_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_SESSION_DATE_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_VENUE_ID_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_WASHED_OUT_COLUMN

@Entity(tableName = SESSION_TABLE_NAME, foreignKeys = [
    ForeignKey(entity = VenueEntity::class,
        childColumns = [SESSION_TABLE_VENUE_ID_COLUMN],
        parentColumns = ["uid"])
])
data class SessionEntity (
    @PrimaryKey(autoGenerate = true) val uid: Long,
    // Session date is the date on which the player played. Stored as unix epoch timestamp
    // Includes date and start time
    @ColumnInfo(name= SESSION_TABLE_SESSION_DATE_COLUMN) val sessionDate: Long,
    // Session duration stored as decimal
    @ColumnInfo(name = SESSION_TABLE_DURATION_COLUMN) val sessionDuration: Float,
    // Playing type i.e. singles/doubles/threes
    @ColumnInfo(name = SESSION_TABLE_PLAYING_FORMAT_COLUMN) val sessionPlayingFormat: String = "Singles",
    @ColumnInfo(name = SESSION_TABLE_COST_COLUMN) val sessionCost: Float,
    @ColumnInfo(name = SESSION_TABLE_NOTES_COLUMN) val sessionNotes: String,
    // Rally, Set, Both
    @ColumnInfo(name = SESSION_TABLE_PLAYING_STRUCTURE_COLUMN) val sessionPlayingStructure: String,
    @ColumnInfo(name = SESSION_TABLE_REACHED_TIME_COLUMN) val sessionReachedOnTime: Boolean,
    @ColumnInfo(name = SESSION_TABLE_WASHED_OUT_COLUMN) val sessionWashedOut: Boolean,
    // Low, Medium, High
    @ColumnInfo(name = SESSION_TABLE_QUALITY_OF_TENNIS_COLUMN) val sessionQualityOfTennis: String,
    @ColumnInfo(name = SESSION_TABLE_NUMBER_OF_STEPS_COLUMN) val sessionNumberOfSteps: Int,
    @ColumnInfo(name = SESSION_TABLE_NUMBER_OF_SHOTS_PLAYED_COLUMN) val sessionNumberOfShotsPlayed: Int,

    @ColumnInfo(name = SESSION_TABLE_VENUE_ID_COLUMN) val venueId: Long, // Foreign key column to venue

    // Payment columns
    @ColumnInfo(name = SESSION_TABLE_PAYMENT_AMOUNT_DUE_COLUMN) val sessionAmountDue: Float = 0f,
    @ColumnInfo(name = SESSION_TABLE_PAYMENT_HAS_PAID_COLUMN) val sessionHasPaid: Boolean = false,
    @ColumnInfo(name = SESSION_TABLE_PAYMENT_PAID_DATE_COLUMN) val sessionPaidDate: String = ""

    )

