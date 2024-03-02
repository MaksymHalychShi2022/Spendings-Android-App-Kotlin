package com.example.spendings.data.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.spendings.utils.UnitOfMeasurement

@Entity(tableName = "products",
    indices = [Index(value=["name"], unique=true)])
data class Product (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val defaultUnit: UnitOfMeasurement
)