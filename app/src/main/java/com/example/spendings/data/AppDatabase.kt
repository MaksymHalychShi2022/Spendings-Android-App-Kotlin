package com.example.spendings.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spendings.data.dao.ProductDao
import com.example.spendings.data.dao.SpendingDao
import com.example.spendings.data.models.Product
import com.example.spendings.data.models.Spending

@Database(entities = [Product::class, Spending::class], version = 3)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun spendingDao(): SpendingDao
}