package com.example.spendings.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spendings.utils.UnitOfMeasurement

@Entity(tableName = "spendings")
data class Spending(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: Int,
    val moneySpent: Double,
    val quantity: Double,
    val unit: UnitOfMeasurement,
    val timestamp: Long
)
