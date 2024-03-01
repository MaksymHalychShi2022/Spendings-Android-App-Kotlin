package com.example.spendings.ui

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.spendings.data.DatabaseClient
import com.example.spendings.data.models.Product
import com.example.spendings.data.models.Spending
import com.example.spendings.data.models.SpendingWithProductName
import com.example.spendings.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

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

    fun createProduct(newProduct: Product): LiveData<Resource<Unit>> {
        val liveData = MutableLiveData<Resource<Unit>>()
        viewModelScope.launch(Dispatchers.IO) {
            liveData.postValue(Resource.Loading())
            try {
                db.productDao().insert(newProduct)
                liveData.postValue(Resource.Success(Unit))
            } catch (e: SQLiteConstraintException) {
                liveData.postValue(Resource.Error("Product already exist"))
            } catch (e: Exception) {
                liveData.postValue(Resource.Error("Unknown error"))
            }
        }
        return liveData
    }

    fun deleteProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        db.productDao().delete(product)
    }

    fun deleteSpendingsById(spendingId: Int) = viewModelScope.launch(Dispatchers.IO) {
        db.spendingDao().deleteById(spendingId)
    }
}