package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.helper.SwipeCallback
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.helper.SwipeHelper
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.generalviewmodel.CartsViewModel
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.helper.Utils
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.service.AlarmReceiver
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen.adapter.CartsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class EditCartList : Fragment(), View.OnClickListener {
    private lateinit var alarmManager: AlarmManager
    private val activityTitle = "Edit Carts"
    private var navController: NavController? = null
    private lateinit var cartsViewModel: CartsViewModel
    private lateinit var tvActiveCartCount: TextView
    private lateinit var rvCarts: RecyclerView
    private lateinit var rvAdapter: CartsAdapter

    private fun setActivityTitle() {
        (activity as AppCompatActivity?)?.supportActionBar?.title = activityTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartsViewModel = CartsViewModel.getInstance(this, requireActivity().application)
        alarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
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
            val activeSum = getActiveCartsCount(carts)
            tvActiveCartCount.apply {
                if (activeSum == 0) alpha = 0F
                else {
                    alpha = 1F
                    text = activeSum.toString()
                }
            }
            setAlarms(carts)
        })
    }

    override fun onResume() {
        super.onResume()
        setActivityTitle()
    }

    private fun getActiveCartsCount(carts: List<Cart>): Int {
        return carts.map {
            if (it.active && Utils.isCalendarNotPassedAndToday(it.calendar)) 1 else 0
        }.sum()
    }

    private fun setupRecyclerView() {
        rvAdapter = CartsAdapter(requireActivity().application)
        rvCarts.apply {
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically() = false
            }
            adapter = rvAdapter
            itemAnimator = DefaultItemAnimator().apply {
                addDuration = 500
                removeDuration = 300
            }
        }
        ItemTouchHelper(SwipeHelper(onClickCallback())).attachToRecyclerView(rvCarts)
    }

    private fun onClickCallback() = object : SwipeCallback {
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

    override fun onClick(v: View?) {
        if (v?.id == R.id.fab_add) {
            navController?.navigate(EditCartListDirections.actionEditCartListToAddEditCart())
        }
    }

    private fun setAlarms(carts: List<Cart>) {
        CoroutineScope(Dispatchers.Default).launch {
            carts.forEach {
                if (it.active && Utils.isCalendarNotPassedAndToday(it.calendar)) startAlarm(it)
                else cancelAlarm(it)
            }
        }
    }

    private suspend fun startAlarm(cart: Cart) {
        val total = cartsViewModel.cartRepo.getTotalPrice(cart)
        val pendingIntent = AlarmReceiver.buildPendingIntent(requireContext(), cart, total)
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            cart.calendar.timeInMillis,
            pendingIntent
        )
    }

    private fun cancelAlarm(cart: Cart) {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = AlarmReceiver.buildPCancellingPendingIntent(requireContext(), cart)
        alarmManager.cancel(pendingIntent)
    }
}