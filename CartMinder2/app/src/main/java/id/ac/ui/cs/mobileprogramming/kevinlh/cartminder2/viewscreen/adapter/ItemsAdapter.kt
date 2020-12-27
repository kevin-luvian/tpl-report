package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Item

class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
    companion object {
        @Volatile
        private var instance: ItemsAdapter? = null
        fun getInstance() = instance ?: ItemsAdapter().also { instance = it }
    }

    var listener: ClickListener? = null
    var items: List<Item> = listOf()
        set(value) {
            val diffCallback = DiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setItem(items[position])
        listener?.let {
            holder.itemView.setOnClickListener { _ ->
                it.onItemClicked(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface ClickListener {
        fun onItemClicked(item: Item)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle = view.findViewById<TextView>(R.id.title)
        private val tvPrice = view.findViewById<TextView>(R.id.price)

        fun setItem(item: Item) {
            val price = "${tvPrice.context.getString(R.string.currency)} ${item.price}"
            tvTitle.text = item.title
            tvPrice.text = price
        }
    }

    private class DiffCallback(
        private val oldList: List<Item>,
        private val newList: List<Item>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size

        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}