package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.adapter.EditCartsAdapter
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.helper.SwipeCallback
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.helper.SwipeHelper
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.generalviewmodel.CartsViewModel
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.generalviewmodel.CartsViewModelFactory


class EditCartList : Fragment(), View.OnClickListener {
    private val activityTitle = "Edit Carts"
    private var navController: NavController? = null
    private lateinit var cartsViewModel: CartsViewModel
    private lateinit var tvActiveCartCount: TextView
    private lateinit var rvCarts: RecyclerView
    private lateinit var rvAdapter: EditCartsAdapter

    private fun setActivityTitle() {
        (activity as AppCompatActivity?)?.supportActionBar?.title = activityTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = CartsViewModelFactory(requireActivity().application)
        cartsViewModel = ViewModelProvider(this, factory)
            .get(CartsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_cart_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        tvActiveCartCount = view.findViewById(R.id.active_cart_count)
        rvCarts = view.findViewById(R.id.carts_recycler_view)
        view.findViewById<FloatingActionButton>(R.id.fab_add).setOnClickListener(this)
        setupRecyclerView()
        cartsViewModel.carts.observe(viewLifecycleOwner, { carts ->
            rvAdapter.carts = carts
            if (carts.isEmpty()) {
                tvActiveCartCount.alpha = 0F
            } else {
                tvActiveCartCount.text = carts.size.toString()
                tvActiveCartCount.alpha = 1F
            }
        })
    }

    override fun onResume() {
        super.onResume()
        setActivityTitle()
    }

    private fun setupRecyclerView() {
        rvCarts.layoutManager = getLayoutManager()
        rvAdapter = EditCartsAdapter(requireActivity().application)
        rvCarts.adapter = rvAdapter
        val itemTouchHelper = ItemTouchHelper(SwipeHelper(onClickCallback()))
        itemTouchHelper.attachToRecyclerView(rvCarts)
        val itemAnimator: ItemAnimator = DefaultItemAnimator()
        itemAnimator.addDuration = 500
        itemAnimator.removeDuration = 300
        rvCarts.itemAnimator = itemAnimator
    }

    private fun onClickCallback(): SwipeCallback {
        return object : SwipeCallback {
            override fun onDeleteClicked(position: Int) {
                rvAdapter.carts[position].run {
                    cartsViewModel.deleteCart(this)
                }
            }

            override fun onEditClicked(position: Int) {
                rvAdapter.carts[position].run {
                    navController?.navigate(
                        EditCartListDirections
                            .actionEditCartListToAddEditCart(this)
                    )
                }
            }
        }
    }

    private fun getLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fab_add) {
            navController?.navigate(EditCartListDirections.actionEditCartListToAddEditCart())
        }
    }
}