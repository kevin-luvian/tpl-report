package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.generalviewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.CartRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.ItemRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart

class CartsViewModel(application: Application) : ViewModel() {
    val cartRepo = CartRepository(application)
    val itemRepo = ItemRepository(application)
    val carts: LiveData<List<Cart>>

    init {
        carts = cartRepo.getLiveCarts()
    }

    fun deleteCart(cart: Cart) {
        cartRepo.deleteCart(cart)
    }
}