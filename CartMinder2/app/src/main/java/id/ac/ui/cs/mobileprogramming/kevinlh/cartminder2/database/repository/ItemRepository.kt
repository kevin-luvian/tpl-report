package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.CartMinderDatabase
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.dao.ItemDao
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemRepository(application: Application) {
    private val itemDao: ItemDao
    val liveItems: LiveData<List<Item>>

    init {
        val database = CartMinderDatabase.getInstance(application)
        itemDao = database.itemDao
        liveItems = itemDao.findAll().asLiveData()
    }

    suspend fun insertItem(item: Item): Long {
        return itemDao.insert(item)
    }

    fun updateItem(item: Item) {
        CoroutineScope(Dispatchers.IO).launch { itemDao.update(item) }
    }

    fun deleteItem(item: Item) {
        CoroutineScope(Dispatchers.IO).launch { itemDao.delete(item) }
    }

    suspend fun findByCartId(id: Long): List<Item> {
        return itemDao.findByCartId(id)
    }

    companion object {
        private var instance: ItemRepository? = null

        fun getInstance(application: Application): ItemRepository {
            return instance ?: ItemRepository(application)
        }
    }
}