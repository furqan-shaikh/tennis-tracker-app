package com.reversecurrent.tennistracker.dal.entities

data class BasicTennisAnalyticsEntity(
    val totalHoursPlayed: Float,
    val totalShotsPlayed: Int,
    val totalStepsTaken: Int
)

data class PlayerPlayedWithEntity(
    val playerName: String,
    val playerId: Long,
    val numberOfTimesPlayed: Int,
    val totalHoursPlayed: Float = 0f,
)