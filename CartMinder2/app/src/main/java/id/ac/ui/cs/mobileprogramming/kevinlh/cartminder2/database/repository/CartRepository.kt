package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.CartMinderDatabase
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.dao.CartDao
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.dao.ItemDao
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartRepository(application: Application) {
    private val cartDao: CartDao
    private val itemDao: ItemDao

    init {
        val database = CartMinderDatabase.getInstance(application)
        cartDao = database.cartDao
        itemDao = database.itemDao
    }

    suspend fun findById(cartId: Long): Cart = cartDao.findById(cartId)

    suspend fun insertCart(cart: Cart): Long = cartDao.insert(cart)

    suspend fun getTotalPrice(cart: Cart): Int {
        var total = 0
        val items = itemDao.findByCartId(cart.id)
        for (i in items) {
            total += i.price
        }
        return total
    }

    fun getLiveCarts(): LiveData<List<Cart>> = cartDao.getCarts().asLiveData()

    fun updateCart(cart: Cart) = CoroutineScope(Dispatchers.IO).launch { cartDao.update(cart) }

    fun deleteCart(cart: Cart) = CoroutineScope(Dispatchers.IO).launch { cartDao.delete(cart) }

    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            cartDao.findAll().forEach {
                deleteCart(it)
            }
        }
    }

    companion object {
        private var instance: CartRepository? = null

        fun getInstance(application: Application): CartRepository {
            return instance ?: CartRepository(application)
        }
    }
}