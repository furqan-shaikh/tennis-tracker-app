package com.reversecurrent.tennistracker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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

data class Session (
    val sessionDate: String,
    val sessionDuration: Float,
    val sessionPlayingFormat: String = "Singles",
    val sessionCost: Float,
    val sessionNotes: String = "",
    val sessionPlayingStructure: String = "Rally",
    val sessionReachedOnTime: Boolean = true,
    val sessionWashedOut: Boolean = false,
    val sessionQualityOfTennis: String,
    val sessionNumberOfSteps: Int,
    val sessionNumberOfShotsPlayed: Int,

    val players: List<Long>,
    val venueId: Long,

)

data class PlayerPayment (
    val playerName: String,
    val playerId: Int,
    val paymentAmount: Float,
    val paymentDate: String,
    val hasPaid: Boolean
)

const val SESSION_DATE_FORMAT = "yyyy-MM-dd HH:mm"