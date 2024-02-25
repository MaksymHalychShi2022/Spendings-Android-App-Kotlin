package com.example.spendings.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.spendings.data.DatabaseClient
import com.example.spendings.data.models.Product
import com.example.spendings.data.models.Spending
import com.example.spendings.data.models.SpendingWithProductName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    app: Application
) : AndroidViewModel(app) {
    private val db = DatabaseClient.getDatabase(app)

    private val _spendingList = MutableLiveData<List<SpendingWithProductName>>()
    val spendingList: LiveData<List<SpendingWithProductName>> = _spendingList

    val allProducts: LiveData<List<Product>> = db.productDao().getAll()
    val allSpendingNotes = db.spendingDao().getSpendingNotes()

    fun getSpendings(timestamp: Long) = db.spendingDao().getSpendingsWithProductNames(timestamp)

    fun addSpending(newSpending: SpendingWithProductName) = viewModelScope.launch(Dispatchers.IO) {
        val currentList = _spendingList.value ?: emptyList()
        _spendingList.postValue(currentList + newSpending)
    }

    fun addSpendingList(spendingList: List<Spending>) = viewModelScope.launch(Dispatchers.IO) {
        db.spendingDao().insertAll(spendingList)
    }

    fun clearSpendingList() {
        _spendingList.postValue(emptyList())
    }

    fun createProduct(newProduct: Product) = viewModelScope.launch(Dispatchers.IO) {
        db.productDao().insert(newProduct)
    }

    fun deleteProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        db.productDao().delete(product)
    }

    fun deleteSpendingsById(spendingId: Int) = viewModelScope.launch(Dispatchers.IO) {
        db.spendingDao().deleteById(spendingId)
    }
}