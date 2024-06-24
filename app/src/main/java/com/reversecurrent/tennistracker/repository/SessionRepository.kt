package com.reversecurrent.tennistracker.repository

import android.support.annotation.WorkerThread
import android.util.Log
import com.reversecurrent.tennistracker.dal.database.DatabaseAccessor
import com.reversecurrent.tennistracker.dal.entities.PlayerEntity
import com.reversecurrent.tennistracker.dal.entities.SessionEntity
import com.reversecurrent.tennistracker.dal.entities.SessionPaymentEntity
import com.reversecurrent.tennistracker.models.OutstandingPayment
import com.reversecurrent.tennistracker.models.SESSION_DATE_FORMAT
import com.reversecurrent.tennistracker.models.Session
import com.reversecurrent.tennistracker.utils.fromEpoch
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
                venueId = session.venueId,

                sessionAmountDue = session.sessionAmountDue,
                sessionPaidDate = session.sessionPaidDate,
                sessionHasPaid = session.sessionHasPaid
            )
        Log.i("SessionRepository", sessionEntity.toString())
        val sessionId = DatabaseAccessor.getDatabase(applicationContext = context)
                .sessionDao().insertSessionWithPlayers(sessionEntity, session.players)

        // insert payment details if self booked
        if(session.isSelfBooked){
            val sessionPaymentDao = DatabaseAccessor.getDatabase(applicationContext = context)
                .sessionPaymentDao()
            for(sessionPayment in session.sessionPayments){
                sessionPaymentDao.insertSessionPayment(sessionPaymentEntity = SessionPaymentEntity(
                    uid = 0,
                    sessionId = sessionId,
                    playerId = sessionPayment.playerId,
                    paymentDate = sessionPayment.paymentDate,
                    paymentAmountDue = sessionPayment.paymentAmount,
                    hasPaid = sessionPayment.hasPaid
                ))
            }
        }
    }

    @WorkerThread
    suspend fun getAllOutstandingPayments(context: Any): List<OutstandingPayment> {
        val outstandingSessionPayments: List<SessionPaymentEntity> = DatabaseAccessor.getDatabase(applicationContext = context)
            .sessionPaymentDao().getAllOutstandingPayments()

        val outstandingPayments = mutableListOf<OutstandingPayment>()
        for(outstandingSessionPayment in outstandingSessionPayments){
            // for each outstanding payment, get the player details
            // for each outstanding payment, get the session details
            val player: PlayerEntity = DatabaseAccessor.getDatabase(applicationContext = context)
                .playerDao().getById(outstandingSessionPayment.playerId)
            val session: SessionEntity = DatabaseAccessor.getDatabase(applicationContext = context)
                .sessionDao().getById(outstandingSessionPayment.sessionId)

            outstandingPayments.add(OutstandingPayment(
                sessionDate = fromEpoch(session.sessionDate, SESSION_DATE_FORMAT),
                playerName = player.playerName,
                paymentAmount = outstandingSessionPayment.paymentAmountDue
            ))

        }
        return outstandingPayments
    }
}