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

    private val _spendingMutableList = mutableListOf<SpendingWithProductName>()
    private val _spendingList = MutableLiveData<List<SpendingWithProductName>>()
    val spendingList: LiveData<List<SpendingWithProductName>> = _spendingList
    var spendingListTimestamp: Long? = null

    val allProducts: LiveData<List<Product>> = db.productDao().getAll()
    val allSpendingNotes = db.spendingDao().getSpendingNotes()

    fun getSpendings(timestamp: Long) = db.spendingDao().getSpendingsWithProductNames(timestamp)

    fun updateSpendingList(spending: SpendingWithProductName) =
        viewModelScope.launch(Dispatchers.IO) {
            val existingIndex =
                _spendingMutableList.indexOfFirst { it.productId == spending.productId }

            if (existingIndex != -1) {
                // Item exists, update it
                _spendingMutableList[existingIndex] = spending
            } else {
                // Item does not exist, add it to the list
                _spendingMutableList.add(spending)
            }

            // Notify observers of the change
            _spendingList.postValue(ArrayList(_spendingMutableList))
        }

    fun addSpendingList(spendingList: List<Spending>) = viewModelScope.launch(Dispatchers.IO) {
        db.spendingDao().insertAll(spendingList)
    }

    fun clearSpendingList() {
        _spendingList.postValue(emptyList())
        spendingListTimestamp = null
    }

    fun createProduct(newProduct: Product): LiveData<Resource<Unit>> {
        val liveData = MutableLiveData<Resource<Unit>>()
        viewModelScope.launch(Dispatchers.IO) {
            liveData.postValue(Resource.Loading())
            try {
                db.productDao().insert(newProduct)
                liveData.postValue(Resource.Success(Unit))
            } catch (e: SQLiteConstraintException) {
                liveData.postValue(Resource.Error("Product with such name already exist"))
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

    fun getProductByName(productName: String): Product? {
        val products = allProducts.value ?: emptyList()
        return products.find { it.name == productName }
    }
}