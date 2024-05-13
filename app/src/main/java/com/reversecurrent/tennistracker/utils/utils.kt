package com.reversecurrent.tennistracker.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
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

fun safeStrToInt(value: String, defaultValue: Int = 0) : Int {
    return value.toIntOrNull() ?: return defaultValue
}

fun safeStrToFloat(value: String, defaultValue: Float = 0.0F) : Float {
    return value.toFloatOrNull() ?: return defaultValue
}