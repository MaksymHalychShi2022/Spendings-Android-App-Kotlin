package com.example.spendings.data.models

data class SpendingWithProductName(
    val id: Int = 0,
    val productId: Int,
    val productName: String,
    val moneySpent: Double,
    val quantity: Double,
    val unit: String,
    val timestamp: Long
)

