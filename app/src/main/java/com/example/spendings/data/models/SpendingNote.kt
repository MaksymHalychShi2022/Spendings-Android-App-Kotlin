package com.example.spendings.data.models

import com.example.spendings.utils.DateTimeUtil

data class SpendingNote(
    val timestamp: Long,
    val moneySpent: Double
) {
    fun getInfo(): String {
        return "${DateTimeUtil.unixTimestampToDateTimeStr(timestamp, "dd-MM HH:mm")}/$moneySpent"
    }
}
