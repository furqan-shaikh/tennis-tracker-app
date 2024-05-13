package com.reversecurrent.tennistracker.repository

import android.support.annotation.WorkerThread
import android.util.Log
import com.reversecurrent.tennistracker.dal.database.DatabaseAccessor
import com.reversecurrent.tennistracker.dal.entities.PlayerEntity
import com.reversecurrent.tennistracker.dal.entities.SessionEntity
import com.reversecurrent.tennistracker.models.Player
import com.reversecurrent.tennistracker.models.SESSION_DATE_FORMAT
import com.reversecurrent.tennistracker.models.Session
import com.reversecurrent.tennistracker.utils.toEpoch

class SessionRepository {
    @WorkerThread
    suspend fun insertSession(context: Any, session: Session) {
        val sessionDate = toEpoch(session.sessionDate, SESSION_DATE_FORMAT)
            ?: throw Exception("Invalid date format")
        val sessionEntity = SessionEntity(
                sessionNotes = session.sessionNotes,
                sessionNumberOfSteps = session.sessionNumberOfSteps,
                sessionPlayingFormat = session.sessionPlayingFormat,
                sessionReachedOnTime = session.sessionReachedOnTime,
                sessionWashedOut = session.sessionWashedOut,
                sessionCost = session.sessionCost,
                sessionDuration = session.sessionDuration,
                sessionDate = sessionDate,
                sessionQualityOfTennis = session.sessionQualityOfTennis,
                sessionPlayingStructure = session.sessionPlayingStructure,
                sessionNumberOfShotsPlayed = session.sessionNumberOfShotsPlayed,
                uid = 0,
                venueId = session.venueId
            )
        Log.i("SessionRepository", sessionEntity.toString())
        Log.i("SessionRepository", "Before inserting")
        DatabaseAccessor.getDatabase(applicationContext = context)
                .sessionDao().insertSessionWithPlayers(sessionEntity, session.players)


    }
}