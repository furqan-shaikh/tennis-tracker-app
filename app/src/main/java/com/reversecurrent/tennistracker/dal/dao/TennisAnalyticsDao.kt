package com.reversecurrent.tennistracker.dal.dao

import androidx.room.Dao
import androidx.room.Query
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_NAME
import com.reversecurrent.tennistracker.dal.PLAYER_TABLE_NAME_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_PLAYER_JOIN_TABLE_PLAYER_ID_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_DURATION_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_NAME
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_NUMBER_OF_SHOTS_PLAYED_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_TABLE_NUMBER_OF_STEPS_COLUMN
import com.reversecurrent.tennistracker.dal.SESSION_PLAYER_JOIN_TABLE_NAME
import com.reversecurrent.tennistracker.dal.SESSION_PLAYER_JOIN_TABLE_SESSION_ID_COLUMN
import com.reversecurrent.tennistracker.dal.entities.BasicTennisAnalyticsEntity
import com.reversecurrent.tennistracker.dal.entities.PlayerPlayedWithEntity

@Dao
interface TennisAnalyticsDao {
    @Query("SELECT SUM($SESSION_TABLE_DURATION_COLUMN) as totalHoursPlayed, SUM($SESSION_TABLE_NUMBER_OF_SHOTS_PLAYED_COLUMN) as totalShotsPlayed, SUM($SESSION_TABLE_NUMBER_OF_STEPS_COLUMN) as totalStepsTaken FROM $SESSION_TABLE_NAME")
    suspend fun getBasicStats(): BasicTennisAnalyticsEntity

    @Query("SELECT ps.$SESSION_PLAYER_JOIN_TABLE_PLAYER_ID_COLUMN as playerId," +
            " p.$PLAYER_TABLE_NAME_COLUMN as playerName," +
            " COUNT(ps.$SESSION_PLAYER_JOIN_TABLE_PLAYER_ID_COLUMN) AS numberOfTimesPlayed," +
            " SUM(s.$SESSION_TABLE_DURATION_COLUMN) AS totalHoursPlayed" +
            " FROM $SESSION_PLAYER_JOIN_TABLE_NAME ps INNER JOIN $PLAYER_TABLE_NAME p ON ps.$SESSION_PLAYER_JOIN_TABLE_PLAYER_ID_COLUMN = p.uid " +
            " INNER JOIN $SESSION_TABLE_NAME s ON ps.$SESSION_PLAYER_JOIN_TABLE_SESSION_ID_COLUMN = s.uid" +
            " GROUP BY ps.$SESSION_PLAYER_JOIN_TABLE_PLAYER_ID_COLUMN, p.$PLAYER_TABLE_NAME_COLUMN" +
            " ORDER BY numberOfTimesPlayed DESC"
    )
    suspend fun getPlayedWithStats(): List<PlayerPlayedWithEntity>
}