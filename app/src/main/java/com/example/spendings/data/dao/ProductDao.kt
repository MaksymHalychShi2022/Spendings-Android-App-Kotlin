package com.example.spendings.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.spendings.data.models.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAll(): LiveData<List<Product>>

    @Insert
    fun insert(products: Product)

    @Delete
    fun delete(product: Product)
}