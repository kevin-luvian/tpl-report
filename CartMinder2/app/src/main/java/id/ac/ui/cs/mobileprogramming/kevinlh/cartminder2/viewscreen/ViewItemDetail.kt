package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.helper.FileHelper
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.helper.Utils
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Detail
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Item
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen.adapter.TagsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ViewItemDetail : Fragment() {
    private lateinit var item: Item
    private lateinit var detail: Detail
    private lateinit var rvTags: RecyclerView
    private lateinit var rvAdapter: TagsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args: ViewItemDetailArgs by navArgs()
        item = args.item
        detail = args.detail
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_item_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val priceText = "${
            getString(R.string.currency)
        } ${
            Utils.numberToCurrencyFormat(item.price.toDouble())
        }"
        view.findViewById<TextView>(R.id.item_title).text = item.title
        view.findViewById<TextView>(R.id.item_price).text = priceText
        view.findViewById<TextView>(R.id.detail_description).text = detail.description

        CoroutineScope(Dispatchers.Default).launch {
            detail.imagePath?.let { path ->
                val file = File(path)
                if (file.exists()) {
                    val uri = FileHelper.createImageUri(requireContext(), file)
                    withContext(Dispatchers.Main) {
                        view.findViewById<ImageView>(R.id.detail_image).setImageURI(uri)
                    }
                }
            }
        }

        rvTags = view.findViewById(R.id.tags_recycler_view)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        rvAdapter = TagsAdapter().also { it.tags = detail.tags }
        rvTags.apply {
            layoutManager = linearLayoutManager()
            adapter = rvAdapter
        }
    }

    private fun linearLayoutManager() = object : LinearLayoutManager(context) {
        override fun setOrientation(orientation: Int) = super.setOrientation(HORIZONTAL)

        override fun setInitialPrefetchItemCount(itemCount: Int) =
            super.setInitialPrefetchItemCount(4)
    }
}