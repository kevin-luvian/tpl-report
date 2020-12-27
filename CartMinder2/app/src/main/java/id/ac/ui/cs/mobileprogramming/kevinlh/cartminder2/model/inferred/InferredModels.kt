package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.inferred

import com.google.gson.annotations.SerializedName
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Detail
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Item


data class ItemWithDetail(val item: Item = Item(), var detail: Detail? = null) {
    override fun equals(other: Any?): Boolean {
        if (other is ItemWithDetail) {
            return item == other.item && detail == other.detail
        }
        throw RuntimeException("can't compare to non ItemWithDetail object")
    }
}


data class CartApiModel(
    @SerializedName("_id")
    var id: String = "none",
    var cart: Cart? = null,
    var iwdList: List<ItemWithDetail?> = listOf()
) {
    override fun toString(): String {
        val s = StringBuilder()
        s.append("\nCartRes:: $id\n")
        s.append("$cart")
        iwdList.forEach { iwd ->
            iwd?.run {
                s.append("$item")
                s.append("$detail")
            }
        }
        return s.toString()
    }
}

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