package com.reversecurrent.tennistracker.dal.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_NAME_COLUMN
import com.reversecurrent.tennistracker.dal.VENUE_TABLE_ADDRESS_COLUMN
import com.reversecurrent.tennistracker.dal.VENUE_TABLE_ALIAS_COLUMN
import com.reversecurrent.tennistracker.dal.VENUE_TABLE_COACHING_AVAILABLE_COLUMN
import com.reversecurrent.tennistracker.dal.VENUE_TABLE_COST_PER_HOUR_COLUMN
import com.reversecurrent.tennistracker.dal.VENUE_TABLE_COURT_TYPE_COLUMN
import com.reversecurrent.tennistracker.dal.VENUE_TABLE_NAME
import com.reversecurrent.tennistracker.dal.VENUE_TABLE_NAME_COLUMN
import com.reversecurrent.tennistracker.dal.VENUE_TABLE_NUMBER_OF_COURTS_COLUMN
import com.reversecurrent.tennistracker.dal.VENUE_TABLE_PRIMARY_CONTACT_NAME_COLUMN
import com.reversecurrent.tennistracker.dal.VENUE_TABLE_PRIMARY_CONTACT_NUMBER_COLUMN


@Entity(tableName = VENUE_TABLE_NAME)
data class VenueEntity (
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name= VENUE_TABLE_NAME_COLUMN) val venueName: String,
    @ColumnInfo(name = VENUE_TABLE_ALIAS_COLUMN) val venueAlias: String = "",
    @ColumnInfo(name = VENUE_TABLE_PRIMARY_CONTACT_NUMBER_COLUMN) val primaryContactNumber: String,
    @ColumnInfo(name = VENUE_TABLE_PRIMARY_CONTACT_NAME_COLUMN) val primaryContactName: String,
    @ColumnInfo(name = VENUE_TABLE_ADDRESS_COLUMN) val address: String,
    @ColumnInfo(name = VENUE_TABLE_NUMBER_OF_COURTS_COLUMN) val numberOfCourts: String,
    @ColumnInfo(name = VENUE_TABLE_COURT_TYPE_COLUMN) val courtType: String,
    @ColumnInfo(name = VENUE_TABLE_COST_PER_HOUR_COLUMN) val costPerHour: String,
    @ColumnInfo(name = VENUE_TABLE_COACHING_AVAILABLE_COLUMN) val coachingAvailable: Boolean
)