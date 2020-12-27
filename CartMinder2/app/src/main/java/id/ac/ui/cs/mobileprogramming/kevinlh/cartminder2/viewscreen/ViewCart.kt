package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.CartRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.DetailRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.ItemRepository
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
        }
        CoroutineScope(Dispatchers.Default).launch {
            val totalText = "${getString(R.string.currency)} ${cartRepo.getTotalPrice(cart)}"
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
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically() = false
            }
            adapter = itemsAdapter
        }
    }

    private fun createAdapter() = ItemsAdapter.getInstance().apply {
        listener = object : ItemsAdapter.ClickListener {
            override fun onItemClicked(item: Item) {
                CoroutineScope(Dispatchers.IO).launch {
                    val detail = detailRepo.findByItemId(item.id)
                    withContext(Dispatchers.Main) {
                        if (detail == null) {
                            Toast.makeText(
                                requireContext(),
                                "no detail is added",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            navController.navigate(
                                ViewCartDirections.actionViewCartToViewItemDetail(
                                    item,
                                    detail
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}