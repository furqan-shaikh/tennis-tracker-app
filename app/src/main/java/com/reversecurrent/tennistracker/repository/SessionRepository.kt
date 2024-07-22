package com.reversecurrent.tennistracker.repository

import android.os.Build
import android.support.annotation.WorkerThread
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.withTransaction
import com.reversecurrent.tennistracker.dal.database.DatabaseAccessor
import com.reversecurrent.tennistracker.dal.entities.PlayerEntity
import com.reversecurrent.tennistracker.dal.entities.SessionEntity
import com.reversecurrent.tennistracker.dal.entities.SessionPaymentEntity
import com.reversecurrent.tennistracker.dal.entities.SessionSetStatsEntity
import com.reversecurrent.tennistracker.dal.entities.VenueEntity
import com.reversecurrent.tennistracker.models.ONLY_DATE_FORMAT
import com.reversecurrent.tennistracker.models.OutstandingPayment
import com.reversecurrent.tennistracker.models.OutstandingPaymentCourt
import com.reversecurrent.tennistracker.models.PlayerPayment
import com.reversecurrent.tennistracker.models.SESSION_DATE_FORMAT
import com.reversecurrent.tennistracker.models.Session
import com.reversecurrent.tennistracker.models.SessionSummary
import com.reversecurrent.tennistracker.models.SetStats
import com.reversecurrent.tennistracker.utils.fromEpoch
import com.reversecurrent.tennistracker.utils.getDurationString
import com.reversecurrent.tennistracker.utils.toEpoch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt
import kotlin.math.roundToLong

data class InsertSessionResult (
    val sessionId: Long = 0,
    val isSuccessful: Boolean = true,
    val error: String = ""
)
class SessionRepository {
    @WorkerThread
    suspend fun insertSession(context: Any, session: Session): InsertSessionResult {
        return withContext(Dispatchers.IO) {
            val dbClient = DatabaseAccessor.getDatabase(applicationContext = context)
            var sessionId: Long = 0
            try{

                val sessionDate = toEpoch(session.sessionDate, SESSION_DATE_FORMAT)
                    ?: throw Exception("Invalid date format")
                var sessionPaidToCourtDate = 0L
                if(session.sessionPaidToCourtDate.isNotEmpty()) {
                    sessionPaidToCourtDate = toEpoch(session.sessionPaidToCourtDate, ONLY_DATE_FORMAT)
                        ?: throw Exception("Invalid date format")
                }

                dbClient.withTransaction {
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
                        uid = session.sessionId,
                        venueId = session.venueId,

                        sessionAmountDue = session.sessionAmountDue,
                        sessionPaidDate = session.sessionPaidDate,
                        sessionHasPaid = session.sessionHasPaid,
                        sessionBookedBy = session.bookedBy,

                        sessionHasPaidToCourt = session.sessionIsPaidToCourt,
                        sessionPaidToCourtDate = sessionPaidToCourtDate
                    )
                    Log.i("SessionRepository", sessionEntity.toString())
                    sessionId = dbClient.sessionDao().insertSessionWithPlayers(sessionEntity, session.players)

                    // insert payment details if self booked
                    if(session.isSelfBooked){
                        val sessionPaymentDao = dbClient.sessionPaymentDao()
                        for(sessionPayment in session.sessionPayments){
                            sessionPaymentDao.insertSessionPayment(sessionPaymentEntity = SessionPaymentEntity(
                                uid = sessionPayment.uid,
                                sessionId = sessionId,
                                playerId = sessionPayment.playerId,
                                paymentDate = sessionPayment.paymentDate,
                                paymentAmountDue = sessionPayment.paymentAmount,
                                hasPaid = sessionPayment.hasPaid
                            ))
                        }
                    }

                }
                return@withContext InsertSessionResult(sessionId = sessionId)
            }
            catch (e: Exception){
                Log.e("SessionRepository", e.toString())
                return@withContext InsertSessionResult(isSuccessful = false, error = e.toString())
            }
        }
    }

    @WorkerThread
    suspend fun getAllOutstandingPaymentsToSelf(context: Any): List<OutstandingPayment> {
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

    @WorkerThread
    suspend fun getAllOutstandingPaymentsForCourts(context: Any): List<OutstandingPaymentCourt> {
        val outstandingSessionPaymentsForCourts: List<SessionEntity> = DatabaseAccessor.getDatabase(applicationContext = context)
            .sessionDao().getAllOutstandingPaymentsForCourts()

        val outstandingPaymentsCourts = mutableListOf<OutstandingPaymentCourt>()
        for(outstandingSessionPaymentForCourt in outstandingSessionPaymentsForCourts){
            val venue: VenueEntity = DatabaseAccessor.getDatabase(applicationContext = context)
                .venueDao().getById(outstandingSessionPaymentForCourt.venueId)


            outstandingPaymentsCourts.add(OutstandingPaymentCourt(
                sessionDate = fromEpoch(outstandingSessionPaymentForCourt.sessionDate, SESSION_DATE_FORMAT),
                paymentAmount = outstandingSessionPaymentForCourt.sessionCost,
                venueName = venue.venueName,
                sessionId = outstandingSessionPaymentForCourt.uid
            ))

        }
        return outstandingPaymentsCourts
    }

    @WorkerThread
    suspend fun getAllOutstandingPaymentsBySelf(context: Any): List<OutstandingPayment> {
        val dbClient = DatabaseAccessor.getDatabase(applicationContext = context)
        val outstandingSessionPaymentsBySelf: List<SessionEntity> = dbClient.sessionDao().getAllOutstandingPaymentsBySelf()


        val outstandingPaymentsBySelf = mutableListOf<OutstandingPayment>()
        for(outstandingSessionPaymentBySelf in outstandingSessionPaymentsBySelf){
            // get session total cost, number of players. to compute paymentAmount, formula : totalCost/numberOfPlayers
            val sessionTotalCost = outstandingSessionPaymentBySelf.sessionCost
            val noOfPlayers = dbClient.sessionPlayersDao().getPlayerIds(outstandingSessionPaymentBySelf.uid).size + 1 // +1 for self
            val paymentAmount = (sessionTotalCost/noOfPlayers)

            outstandingPaymentsBySelf.add(OutstandingPayment(
                sessionDate = fromEpoch(outstandingSessionPaymentBySelf.sessionDate, SESSION_DATE_FORMAT),
                paymentAmount = paymentAmount,
                playerName = outstandingSessionPaymentBySelf.sessionBookedBy
            )
            )
        }

        return outstandingPaymentsBySelf
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @WorkerThread
    suspend fun getAllSessions(context: Any): List<SessionSummary> {
        return withContext(Dispatchers.IO) {
            val dbClient = DatabaseAccessor.getDatabase(applicationContext = context)
            val sessionEntities: List<SessionEntity> = dbClient.sessionDao().getAll()
            val sessions = sessionEntities.map { sessionEntity ->
                //for this session get all its players
                val players = dbClient .sessionPlayersDao().getPlayers(sessionId = sessionEntity.uid)
                val sessionDateStr =
                    fromEpoch(epoch = sessionEntity.sessionDate, format = SESSION_DATE_FORMAT)
                SessionSummary(
                    sessionId = sessionEntity.uid,
                    sessionDate = sessionDateStr,
                    sessionDuration = sessionEntity.sessionDuration,
                    sessionPeriod = getDurationString(epoch= sessionEntity.sessionDate, duration = sessionEntity.sessionDuration),
                    players = players

                )
            }

            return@withContext sessions
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @WorkerThread
    suspend fun getSessionDetails(context: Any, sessionId: Long): Session {
        return withContext(Dispatchers.IO) {
            val dbClient = DatabaseAccessor.getDatabase(applicationContext = context)
            // Get session details
            val sessionEntity: SessionEntity = dbClient.sessionDao().getById(sessionId)
            // Get player ids for the session
            val players: List<Long> = dbClient.sessionPlayersDao().getPlayerIds(sessionId = sessionId)
            // Get payments for the session
            val sessionPayments = dbClient.sessionPaymentDao().getPaymentsForSession(sessionId = sessionId)
            val sessionPlayerPayments = mutableListOf<PlayerPayment>()
            for(sessionPayment in sessionPayments){
                val player: PlayerEntity = dbClient.playerDao().getById(sessionPayment.playerId)
                val playerPayment = PlayerPayment(
                    uid = sessionPayment.uid,
                    playerId = player.uid,
                    playerName = player.playerName,
                    paymentDate = sessionPayment.paymentDate,
                    paymentAmount = sessionPayment.paymentAmountDue,
                    hasPaid = sessionPayment.hasPaid
                )
                sessionPlayerPayments.add(playerPayment)
            }
            // Get set stats for the session
            val sessionSetStats = mutableListOf<SetStats>()
            if (sessionEntity.sessionPlayingStructure.contains("Sets")) {
                val setStats: List<SessionSetStatsEntity> = dbClient.sessionSetStatsDao().getSessionSetStats(sessionId = sessionId)
                Log.i("SessionRepository", setStats.toString())
                setStats.forEach {
                    sessionSetStats.add(
                        SetStats(
                            sessionId = sessionEntity.uid,
                            playerId = it.wonBy,
                            setScore = it.setScore,
                            setAces = it.setAces,
                            setDoubleFaults = it.setDoubleFaults,
                            setWinners = it.setWinners
                        )
                    )
                }
            }


            return@withContext Session(
                sessionId = sessionEntity.uid,
                sessionDate = fromEpoch(sessionEntity.sessionDate, SESSION_DATE_FORMAT),
                sessionDuration = sessionEntity.sessionDuration,
                sessionPlayingFormat = sessionEntity.sessionPlayingFormat,
                sessionCost = sessionEntity.sessionCost,
                sessionNotes = sessionEntity.sessionNotes,
                sessionPlayingStructure = sessionEntity.sessionPlayingStructure,
                sessionReachedOnTime = sessionEntity.sessionReachedOnTime,
                sessionWashedOut = sessionEntity.sessionWashedOut,
                sessionQualityOfTennis = sessionEntity.sessionQualityOfTennis,
                sessionNumberOfSteps = sessionEntity.sessionNumberOfSteps,
                sessionNumberOfShotsPlayed = sessionEntity.sessionNumberOfShotsPlayed,

                isSelfBooked = sessionEntity.sessionBookedBy == "Self",
                sessionAmountDue = sessionEntity.sessionAmountDue,
                sessionPaidDate = sessionEntity.sessionPaidDate,
                sessionHasPaid = sessionEntity.sessionHasPaid,
                players = players,
                sessionPayments = sessionPlayerPayments,
                venueId = sessionEntity.venueId,
                bookedBy = sessionEntity.sessionBookedBy,
                sessionSetStats = sessionSetStats,
                sessionIsPaidToCourt = sessionEntity.sessionHasPaidToCourt,
                sessionPaidToCourtDate = fromEpoch(sessionEntity.sessionPaidToCourtDate, ONLY_DATE_FORMAT)
            )
        }
    }

    @WorkerThread
    suspend fun deleteBySessionId(context: Any, sessionId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try{
                val dbClient = DatabaseAccessor.getDatabase(applicationContext = context)
                dbClient.withTransaction {
                    // 1. Delete session entries from session player join table
                    dbClient.sessionPlayersDao().deleteSession(sessionId = sessionId)
                    // 2. Delete session entries from session payment table
                    dbClient.sessionPaymentDao().deleteBySessionId(sessionId = sessionId)
                    // 3. Delete session set stats from session stats table
                    dbClient.sessionSetStatsDao().deleteSessionSetStats(sessionId = sessionId)
                    // 4. Delete session entries from session table
                    dbClient.sessionDao().deleteBySessionId(sessionId = sessionId)

                }
                Log.i("SessionRepository", "Session $sessionId deleted")
                return@withContext true
            }
            catch (e: Exception){
                Log.e("SessionRepository", e.toString())
                return@withContext false
            }
        }


    }
}