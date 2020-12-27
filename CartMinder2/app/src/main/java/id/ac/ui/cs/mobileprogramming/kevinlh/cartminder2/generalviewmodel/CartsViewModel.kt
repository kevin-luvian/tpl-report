package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.generalviewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
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

    companion object {
        @Volatile
        private var cartsVM: CartsViewModel? = null

        fun getInstance(owner: ViewModelStoreOwner, application: Application): CartsViewModel {
            return cartsVM ?: createCartsVM(owner, application)
        }

        private fun createCartsVM(
            owner: ViewModelStoreOwner,
            application: Application
        ): CartsViewModel {
            ViewModelProvider(owner, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(CartsViewModel::class.java)) {
                        return CartsViewModel(application) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }).get(CartsViewModel::class.java).let {
                cartsVM = it
                return it
            }
        }
    }
}