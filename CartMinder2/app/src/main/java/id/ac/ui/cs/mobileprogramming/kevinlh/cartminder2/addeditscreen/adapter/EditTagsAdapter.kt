package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R

class EditTagsAdapter(private val listener: Listener) :
    RecyclerView.Adapter<EditTagsAdapter.ViewHolder>() {
    var tags: List<String> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.edit_detail_tag_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setTag(tags[position])
        holder.setListener(listener, position)
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    interface Listener {
        fun onDeleteClick(position: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTag: TextView = view.findViewById(R.id.tag)
        private val btnDelete: ImageButton = view.findViewById(R.id.btn_delete)

        fun setTag(tag: String) {
            tvTag.text = tag
        }

        fun setListener(listener: Listener, position: Int) {
            btnDelete.setOnClickListener {
                listener.onDeleteClick(position)
            }
        }
    }
}