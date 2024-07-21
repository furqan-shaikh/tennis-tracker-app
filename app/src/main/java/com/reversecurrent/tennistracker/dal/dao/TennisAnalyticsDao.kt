package com.reversecurrent.tennistracker.dal.dao

import androidx.room.Dao
import androidx.room.Query
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_NAME
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_NAME_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_DURATION_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_NAME
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_NUMBER_OF_SHOTS_PLAYED_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_NUMBER_OF_STEPS_COLUMN
import com.reversecurrent.tennistracker.dal.entities.BasicTennisAnalyticsEntity
import com.reversecurrent.tennistracker.dal.entities.PlayerPlayedWithEntity

@Dao
interface TennisAnalyticsDao {
    @Query("SELECT SUM($SESSION_TABLE_DURATION_COLUMN) as totalHoursPlayed, SUM($SESSION_TABLE_NUMBER_OF_SHOTS_PLAYED_COLUMN) as totalShotsPlayed, SUM($SESSION_TABLE_NUMBER_OF_STEPS_COLUMN) as totalStepsTaken FROM $SESSION_TABLE_NAME")
    suspend fun getBasicStats(): BasicTennisAnalyticsEntity

//    @Query("SELECT \n" +
//            "    p.$PLAYER_TABLE_NAME_COLUMN, \n" +
//            "    COUNT(s.uid) AS play_count\n" +
//            "FROM \n" +
//            "    $SESSION_TABLE_NAME s\n" +
//            "JOIN \n" +
//            "    $PLAYER_TABLE_NAME p ON s. = p.id\n" +
//            "GROUP BY \n" +
//            "    s.player_id, p.player_name\n" +
//            "ORDER BY \n" +
//            "    play_count DESC\n" +
//            "LIMIT 1;\n")
//    suspend fun getPlayedWithMost(): PlayerPlayedWithEntity
}