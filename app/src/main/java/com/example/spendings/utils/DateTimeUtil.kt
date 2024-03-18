package com.example.spendings.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateTimeUtil {
    fun dateTimeStrToUnixTimestamp(dateTimeStr: String, pattern: String = "dd-MM-yyyy HH:mm"): Long {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val localDateTime = LocalDateTime.parse(dateTimeStr, formatter)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun unixTimestampToDateTimeStr(timestamp: Long, pattern: String = "dd-MM-yyyy HH:mm"): String {
        val formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault())
        return formatter.format(Instant.ofEpochMilli(timestamp))
    }
}