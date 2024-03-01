package com.example.spendings.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AppDatabaseMigrations {
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Create the new table with the unique constraint
            db.execSQL("CREATE TABLE products_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, UNIQUE(name))")

            // Insert data into the new table, preserving the 'id' for the first occurrence of each unique 'name'
            db.execSQL(
                """
        INSERT INTO products_new (id, name)
        SELECT MIN(id) as id, name
        FROM products
        GROUP BY name
                """
            )

            // Drop the old table
            db.execSQL("DROP TABLE products")

            // Rename the new table
            db.execSQL("ALTER TABLE products_new RENAME TO products")

            // Explicitly create the index on the 'name' column for the 'products' table
            db.execSQL("CREATE UNIQUE INDEX index_products_name ON products(name)")
        }
    }

    // Additional migrations can be defined as needed
}