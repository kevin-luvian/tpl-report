package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.CartRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.DetailRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.ItemRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.enums.MarketCategory
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.helper.Utils
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Item
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen.adapter.ItemsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ViewCart : Fragment() {
    private lateinit var navController: NavController
    private lateinit var cartRepo: CartRepository
    private lateinit var itemRepo: ItemRepository
    private lateinit var detailRepo: DetailRepository
    private lateinit var cart: Cart
    private val items = MutableLiveData<List<Item>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args: ViewCartArgs by navArgs()
        requireActivity().run {
            cartRepo = CartRepository.getInstance(application)
            itemRepo = ItemRepository.getInstance(application)
            detailRepo = DetailRepository.getInstance(application)
        }
        cart = args.cart
        CoroutineScope(Dispatchers.IO).launch {
            val cartItems = itemRepo.findByCartId(cart.id)
            withContext(Dispatchers.Main) {
                items.value = cartItems
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        cart.run {
            view.findViewById<TextView>(R.id.cart_title).text = title
            view.findViewById<TextView>(R.id.cart_date).text = Utils.calendarToDateString(calendar)
            view.findViewById<TextView>(R.id.cart_time).text = Utils.calendarToTimeString(calendar)
            view.findViewById<View>(R.id.cart_category_bg)
                .setBackgroundColor(MarketCategory.toColor(category, requireContext()))
            view.findViewById<ImageView>(R.id.cart_category_view)
                .setImageResource(MarketCategory.toImgResource(category))
        }
        CoroutineScope(Dispatchers.Default).launch {
            val totalPrice = cartRepo.getTotalPrice(cart)
            val totalText =
                "${getString(R.string.currency)} ${Utils.numberToCurrencyFormat(totalPrice.toDouble())}"
            withContext(Dispatchers.Main) {
                view.findViewById<TextView>(R.id.cart_total).text = totalText
            }
        }
        setupRecyclerView(view.findViewById(R.id.items_recycler_view))
    }

    private fun setupRecyclerView(rvItems: RecyclerView) {
        val itemsAdapter = createAdapter()
        items.observe(viewLifecycleOwner, {
            itemsAdapter.items = it
        })
        rvItems.apply {
            layoutManager = createLayoutManager()
            adapter = itemsAdapter
        }
    }

    private fun createAdapter() = ItemsAdapter.getInstance().apply {
        listener = object : ItemsAdapter.ClickListener {
            override fun onItemClicked(item: Item) {
                CoroutineScope(Dispatchers.IO).launch {
                    val detail = detailRepo.findByItemId(item.id)
                    withContext(Dispatchers.Main) {
                        if (detail == null)
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.no_detail_message),
                                Toast.LENGTH_SHORT
                            ).show()
                        else
                            navController.navigate(
                                ViewCartDirections
                                    .actionViewCartToViewItemDetail(item, detail)
                            )
                    }
                }
            }
        }
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager =
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            gridLayoutManager()
        else linearLayoutManager()

    private fun gridLayoutManager() = object : GridLayoutManager(requireContext(), 2) {
        override fun canScrollVertically(): Boolean = false
        override fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup?) {
            super.setSpanSizeLookup(object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int = if (position % 2 == 0) 0 else 1
            })
        }
    }

    private fun linearLayoutManager() = object : LinearLayoutManager(requireContext()) {
        override fun canScrollVertically() = false
    }

//        new GridLayoutManager(HomeActivity.this, 2) {
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//
//            @Override
//            public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
//                super.setSpanSizeLookup(new SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        if (position % 2 == 0) return 0;
//                        return 1;
//                    }
//                });
//            }
//        };
//    }
}