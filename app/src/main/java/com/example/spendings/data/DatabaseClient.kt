package com.example.spendings.data

import android.content.Context
import androidx.room.Room

object DatabaseClient {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "example-database"
            )
                .addMigrations(AppDatabaseMigrations.MIGRATION_1_2)
                .build()
        }
        return instance!!
    }
}