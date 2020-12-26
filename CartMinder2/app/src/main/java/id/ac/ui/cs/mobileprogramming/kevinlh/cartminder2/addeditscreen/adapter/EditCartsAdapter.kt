package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.CartRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.generalhelper.Utils
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditCartsAdapter(application: Application) :
    RecyclerView.Adapter<EditCartsAdapter.ViewHolder>() {
    private val cartRepo = CartRepository(application)
    var carts: List<Cart> = listOf()
        set(value) {
            val diffCallback = DiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        carts[position].let { c ->
            holder.setCart(c)
            CoroutineScope(Dispatchers.IO).launch {
                val total = cartRepo.getTotalPrice(c)
                withContext(Dispatchers.Main) {
                    holder.setCartTotal(total)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvCartTitle = view.findViewById<TextView>(R.id.cart_title)
        private val tvCartDate = view.findViewById<TextView>(R.id.cart_date)
        private val tvCartTime = view.findViewById<TextView>(R.id.cart_time)
        private val tvCartTotal = view.findViewById<TextView>(R.id.cart_total)

        fun setCart(cart: Cart) {
            tvCartTitle.text = cart.title
            tvCartDate.text = Utils.calendarToDateString(cart.calendar)
            tvCartTime.text = Utils.calendarToTimeString(cart.calendar)
        }

        fun setCartTotal(total: Int) {
            val totalString = "${tvCartTotal.context.getString(R.string.currency)} $total"
            tvCartTotal.text = totalString
        }
    }

    open class DiffCallback(
        private val oldCarts: List<Cart>,
        private val newCarts: List<Cart>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldCarts.size
        }

        override fun getNewListSize(): Int {
            return newCarts.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCarts[oldItemPosition].id == newCarts[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCarts[oldItemPosition] == newCarts[newItemPosition]
        }
    }
}