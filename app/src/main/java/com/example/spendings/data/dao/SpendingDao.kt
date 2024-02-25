package com.example.spendings.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spendings.data.models.Spending
import com.example.spendings.data.models.SpendingNote
import com.example.spendings.data.models.SpendingWithProductName

@Dao
interface SpendingDao {
    @Query("SELECT * FROM spendings")
    fun getAll(): LiveData<List<Spending>>

    @Query("""
        SELECT s.id, s.productId, p.name AS productName, s.moneySpent, s.timestamp
        FROM spendings s
        INNER JOIN products p ON s.productId = p.id
        WHERE s.timestamp=:timestamp
        ORDER BY s.timestamp DESC
    """)
    fun getSpendingsWithProductNames(timestamp: Long): LiveData<List<SpendingWithProductName>>

    @Query("""
        SELECT timestamp, sum(moneySpent) as moneySpent
        FROM spendings
        GROUP BY timestamp
        ORDER BY timestamp DESC
    """)
    fun getSpendingNotes(): LiveData<List<SpendingNote>>

    @Insert
    fun insert(spending: Spending)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(spendings: List<Spending>)

    @Delete
    fun delete(spending: Spending)

    @Query("DELETE FROM spendings WHERE id = :spendingId")
    fun deleteById(spendingId: Int)
}