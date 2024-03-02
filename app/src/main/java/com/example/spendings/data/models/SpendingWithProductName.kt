package com.example.spendings.data.models

import com.example.spendings.utils.UnitOfMeasurement

data class SpendingWithProductName(
    val id: Int,
    val productId: Int,
    val productName: String,
    val moneySpent: Double,
    val quantity: Double,
    val unit: UnitOfMeasurement,
    val timestamp: Long
)

