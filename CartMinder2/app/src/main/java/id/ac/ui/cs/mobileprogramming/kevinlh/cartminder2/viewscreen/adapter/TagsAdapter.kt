package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R

class TagsAdapter : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {
    var tags: List<String> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.detail_tag_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setTag(tags[position])
    }

    override fun getItemCount(): Int = tags.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTag: TextView = view.findViewById(R.id.tag)

        fun setTag(tag: String) {
            tvTag.text = tag
        }
    }
}