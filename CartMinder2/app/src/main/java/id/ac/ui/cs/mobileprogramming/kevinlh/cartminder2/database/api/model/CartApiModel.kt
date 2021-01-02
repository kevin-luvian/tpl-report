package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.api.model

import com.google.gson.annotations.SerializedName
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.inferred.ItemWithDetail

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