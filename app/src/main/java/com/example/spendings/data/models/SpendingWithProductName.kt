package com.example.spendings.data.models

data class SpendingWithProductName(
    val id: Int,
    val productId: Int,
    val productName: String,
    val moneySpent: Double,
    val timestamp: Long
)

