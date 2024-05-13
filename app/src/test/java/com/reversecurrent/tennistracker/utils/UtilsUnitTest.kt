package com.reversecurrent.tennistracker.utils

import org.junit.Assert
import org.junit.Test

class UtilsUnitTest {
    @Test
    fun toEpoch_ValidFormat() {
        val epoch = toEpoch(dateTimeStr = "2024-04-27 09:00", format = "yyyy-MM-dd HH:mm")
        Assert.assertEquals(epoch, 1714188600000)
    }

    @Test
    fun fromEpoch_ValidFormat() {
        val  dateTimeStr = "2024-04-27 09:00"
        val format = "yyyy-MM-dd HH:mm"
        val epoch = toEpoch(dateTimeStr = dateTimeStr,format = format )
        Assert.assertEquals(dateTimeStr, epoch?.let { fromEpoch(epoch = it, format =format ) })
    }
}