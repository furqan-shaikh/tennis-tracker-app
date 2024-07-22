package com.reversecurrent.tennistracker.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date

@SuppressLint("SimpleDateFormat")
fun toEpoch(dateTimeStr: String, format: String) : Long? {
    val sdf = SimpleDateFormat(format)
    return sdf.parse(dateTimeStr)?.time
}

@SuppressLint("SimpleDateFormat")
fun fromEpoch(epoch: Long, format: String) : String {
    val sdf = SimpleDateFormat(format)
    val date = Date(epoch)
    return sdf.format(date)
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SimpleDateFormat")
fun epochToDate(epoch: Long, format: String) : LocalDateTime? {
//    val sdf = SimpleDateFormat(format)
//    return Date(epoch)
    val instant = Instant.ofEpochMilli(epoch)
    val zoneId = ZoneId.systemDefault()
    val localDateTime = instant.atZone(zoneId).toLocalDateTime()
    return localDateTime
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertMillisToLocalDate(millis: Long) : LocalDate {
    return Instant
        .ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun safeStrToInt(value: String, defaultValue: Int = 0) : Int {
    return value.toIntOrNull() ?: return defaultValue
}

fun safeStrToFloat(value: String, defaultValue: Float = 0.0F) : Float {
    return value.toFloatOrNull() ?: return defaultValue
}

// write a function which takes a list of string and returns the string with comma separated
fun listToString(list: List<String>) : String {
    return list.joinToString(", ")
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDurationString(epoch: Long, duration: Float): String {
    // convert epoch to date time
    val instant = Instant.ofEpochMilli(epoch)
    val zoneId = ZoneId.systemDefault()
    val localDateTime = instant.atZone(zoneId).toLocalDateTime()

    // Add duration to start time to get the end time
    val durationInMinutes = (duration * 60).toLong()
    val endTime = localDateTime.plus(durationInMinutes, ChronoUnit.MINUTES)

    // Get the start time in HH:MM
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val formattedStartTime = localDateTime.format(timeFormatter)

    // Get the end time in HH:MM
    val formattedEndTime = endTime.format(timeFormatter)

    return "$formattedStartTime - $formattedEndTime"

}

fun launchApp(context: android.content.Context, packageName: String) {
    val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        ?: // App is not installed
        return
    context.startActivity(intent)
}