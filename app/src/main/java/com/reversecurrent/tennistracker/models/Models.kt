package com.reversecurrent.tennistracker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

const val PLAYER_INTENT_EXTRA = "player_extra"
const val PLAYER_ACTION_INTENT_EXTRA = "player_action_extra"

enum class PlayerActionEnum {
    ADD, EDIT
}

const val VENUE_INTENT_EXTRA = "venue_extra"
const val VENUE_ACTION_INTENT_EXTRA = "venue_action_extra"

enum class VenueActionEnum {
    ADD, EDIT
}

const val SESSION_INTENT_EXTRA = "session_extra"
const val SESSION_ACTION_INTENT_EXTRA = "session_action_extra"

enum class SessionActionEnum {
    ADD, EDIT
}

const val SESSION_SET_STATS_INTENT_EXTRA = "session_set_stats_extra"
const val SESSION_SET_STATS_ACTION_INTENT_EXTRA = "session_set_stats_action_extra"

enum class SessionSetStatsActionEnum {
    ADD, EDIT
}

const val SELF_PLAYER = "Self"

@Parcelize
data class Player(
    val id: Long = 0,
    val displayName: String,
    val mobileNumber: String = "",
    val playingLevel: String = "Beginner",
    val playedBefore: Boolean = false
) : Parcelable

fun getEmptyPlayer(): Player {
    return Player (
        displayName = ""
    )
}

@Parcelize
data class Venue (
    val venueId: Long = 0,
    val venueName: String,
    val venueAlias: String = "",
    val primaryContactNumber: String,
    val primaryContactName: String,
    val address: String,
    val numberOfCourts: String,
    val courtType: String,
    val costPerHour: String,
    val coachingAvailable: Boolean
): Parcelable

fun getEmptyVenue(): Venue {
    return Venue (
        venueId = 0,
        venueName = "",
        primaryContactNumber = "",
        primaryContactName = "",
        address = "",
        numberOfCourts = "",
        courtType = "",
        costPerHour = "",
        coachingAvailable = true
    )
}

fun getPlayingFormats() : List<String> {
    return listOf("Singles", "Doubles", "Threes")
}

fun getPlayingStructures() : List<String> {
    return listOf("Rally", "Sets", "Mini", "All")
}

@Parcelize
data class Session (
    val sessionId: Long = 0,
    val sessionDate: String,
    val sessionDuration: Float = 1f,
    val sessionPlayingFormat: String = "Singles",
    val sessionCost: Float = 500f,
    val sessionNotes: String = "",
    val sessionPlayingStructure: String = "Rally",
    val sessionSetScores: String = "",
    val sessionReachedOnTime: Boolean = true,
    val sessionWashedOut: Boolean = false,
    val sessionQualityOfTennis: String = "Medium",
    val sessionNumberOfSteps: Int = 500,
    val sessionNumberOfShotsPlayed: Int = 300,
    val sessionAmountDue: Float = 0f,
    val sessionHasPaid: Boolean = false,
    val sessionPaidDate: String = "",
    val isSelfBooked: Boolean = false,
    val bookedBy: String = "",
    val sessionPayments: List<PlayerPayment> = emptyList(),
    val sessionIsPaidToCourt: Boolean = false,
    val sessionPaidToCourtDate: String = "",
    val sessionSetStats: List<SetStats> = emptyList(),

    val players: List<Long> = emptyList(),
    val venueId: Long,

): Parcelable

fun getEmptySession(): Session {
    return Session (
        sessionDate = "",
        sessionDuration = 1.0f,
        sessionPlayingFormat = "Singles",
        sessionCost = 500f,
        venueId = 1
    )
}

data class SessionSummary (
    val sessionId: Long,
    val sessionDate: String,
    val sessionPeriod: String,
    val sessionDuration: Float,
    val players: List<String>
)

@Parcelize
data class PlayerPayment (
    val uid: Long = 0,
    val playerName: String,
    val playerId: Long,
    val paymentAmount: Float = 0.0f,
    val paymentDate: String = Date().toString(),
    val hasPaid: Boolean = false,
): Parcelable

data class OutstandingPayment (
    val paymentAmount: Float = 0.0f,
    val sessionDate: String,
    val playerName: String,
)

data class OutstandingPaymentCourt (
    val paymentAmount: Float = 0.0f,
    val sessionDate: String,
    val venueName: String,
)

@Parcelize
data class SetStats (
    val setScore: String = "",
    val setAces: Int = 0,
    val setWinners: Int = 0,
    val setDoubleFaults: Int = 0,
    val sessionId: Long ,
    val playerId: Long
): Parcelable

@Parcelize
data class SetStatsSession (
    val sessionId: Long ,
    val sessionDisplayName: String,
    val sessionSetStats: List<SetStats> = emptyList(),
): Parcelable

data class PlayerPlayedWith(
    val playerName: String,
    val playerId: Long,
    val numberOfTimesPlayed: Int
)
data class TennisAnalytics(
    val totalHoursPlayed: Float = 0f,
    val totalShotsPlayed: Int = 0,
    val totalStepsTaken: Int = 0,
    val playedWithTheMost: PlayerPlayedWith = PlayerPlayedWith("", 0, 0),
    val playedWithTheLeast: PlayerPlayedWith = PlayerPlayedWith("", 0, 0),
    val totalSetsPlayed: Int = 0,
    val totalSetsWon: Int = 0,
)


const val SESSION_DATE_FORMAT = "yyyy-MM-dd HH:mm"
const val ONLY_DATE_FORMAT = "yyyy-MM-dd"