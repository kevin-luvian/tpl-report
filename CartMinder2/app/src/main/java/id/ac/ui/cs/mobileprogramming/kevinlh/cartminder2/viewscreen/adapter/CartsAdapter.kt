package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen.adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.CartRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.enums.MarketCategory
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.helper.Utils
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartsAdapter(application: Application) :
    RecyclerView.Adapter<CartsAdapter.ViewHolder>() {
    companion object {
        fun getInstance(application: Application): CartsAdapter {
            return CartsAdapter(application)
        }
    }

    private var cartRepo = CartRepository(application)
    var listener: ClickListener? = null
    var carts: List<Cart> = listOf()
        set(value) {
            val diffCallback = DiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    fun replaceCarts(newCarts: List<Cart>) {
        carts = listOf()
        carts = newCarts
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
                holder.switch.setOnClickListener {
                    cartRepo.updateCart(c.apply { active = !active })
                }
                val total = cartRepo.getTotalPrice(c)
                withContext(Dispatchers.Main) {
                    holder.setCartTotal(total)
                }
            }
            listener?.let { holder.setListener(it, position) }
        }
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    interface ClickListener {
        fun onClick(position: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val vCartCategoryBg: View = view.findViewById(R.id.cart_category_bg)
        private val ivCartCategory: ImageView = view.findViewById(R.id.cart_category_view)
        private val tvCartTitle: TextView = view.findViewById(R.id.cart_title)
        private val tvCartDate: TextView = view.findViewById(R.id.cart_date)
        private val tvCartTime: TextView = view.findViewById(R.id.cart_time)
        private val tvCartTotal: TextView = view.findViewById(R.id.cart_total)
        val switch: SwitchMaterial = view.findViewById(R.id.cart_switch)

//        fun setListener(listener: ClickListener){
//            itemView.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    listener.onClick(carts.get(position))
//                }
//            }
//        }

        fun setCart(cart: Cart) {
            cart.run {
                vCartCategoryBg.setBackgroundColor(
                    MarketCategory.toColor(category, vCartCategoryBg.context)
                )
                ivCartCategory.setImageResource(MarketCategory.toImgResource(category))
                tvCartTitle.text = title
                tvCartDate.text = Utils.calendarToDateString(calendar)
                tvCartTime.text = Utils.calendarToTimeString(calendar)
                switch.isChecked = active
            }
        }

        fun setCartTotal(total: Int) {

            val totalString = "${
                tvCartTotal.context.getString(R.string.currency)
            } ${
                Utils.numberToCurrencyFormat(total.toDouble())
            }"
            tvCartTotal.text = totalString
        }

        fun setListener(listener: ClickListener, position: Int) {
            itemView.setOnClickListener {
                listener.onClick(position)
            }
        }
    }

    private class DiffCallback(
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