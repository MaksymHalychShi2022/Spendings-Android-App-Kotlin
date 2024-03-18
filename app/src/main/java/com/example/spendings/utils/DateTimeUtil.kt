package com.example.spendings.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateTimeUtil {
    fun dateTimeStrToUnixTimestamp(dateTimeStr: String): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val localDateTime = LocalDateTime.parse(dateTimeStr, formatter)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun unixTimestampToDateTimeStr(timestamp: Long): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault())
        return formatter.format(Instant.ofEpochMilli(timestamp))
    }
}