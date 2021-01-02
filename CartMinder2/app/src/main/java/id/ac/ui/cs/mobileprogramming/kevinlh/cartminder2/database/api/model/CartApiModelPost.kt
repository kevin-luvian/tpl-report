package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.api.model

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.inferred.ItemWithDetail

data class CartApiModelPost(
    var cart: Cart? = null,
    var iwdList: List<ItemWithDetail?> = listOf()
) {
    override fun equals(other: Any?): Boolean {
        if (other is CartApiModelPost) {
            if (cart != other.cart) return false
            val size = iwdList.size.coerceAtLeast(other.iwdList.size)
            for (i in 0..size) {
                try {
                    if (iwdList[i] != other.iwdList[i]) return false
                } catch (e: IndexOutOfBoundsException) {
                    return false
                }
            }
            return true
        }
        throw RuntimeException("can't compare to non CartApiModelPost object")
    }
}