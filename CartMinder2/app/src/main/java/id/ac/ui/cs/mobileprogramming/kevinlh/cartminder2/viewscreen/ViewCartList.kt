package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.AddEditActivity
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.generalviewmodel.CartsViewModel
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.helper.Utils
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.service.AlarmReceiver
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen.adapter.CartsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ViewCartList : Fragment() {
    private lateinit var activityTitle: String
    private lateinit var navController: NavController
    private lateinit var cartsViewModel: CartsViewModel
    private lateinit var rvAdapter: CartsAdapter
    private lateinit var rvCarts: RecyclerView
    private lateinit var tvActiveCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        cartsViewModel = CartsViewModel.getInstance(this, requireActivity().application)
        activityTitle = resources.getString(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit_menu) {
            val intent = Intent(context, AddEditActivity::class.java)
            startActivity(intent)
        }
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_cart_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        rvCarts = view.findViewById(R.id.carts_recycler_view)
        tvActiveCount = view.findViewById(R.id.active_cart_count)
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        cartsViewModel.carts.observe(viewLifecycleOwner, { carts ->
            rvAdapter.carts = carts
            val activeSum = getActiveCartsCount(carts)
            tvActiveCount.apply {
                if (activeSum == 0) alpha = 0F
                else {
                    alpha = 1F
                    text = activeSum.toString()
                }
            }
            setAlarms(carts)
        })
    }

    private fun getActiveCartsCount(carts: List<Cart>): Int {
        return carts.map {
            if (it.active && Utils.isCalendarNotPassedAndToday(it.calendar)) 1 else 0
        }.sum()
    }

    private fun setupRecyclerView() {
        rvAdapter = createAdapter()
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
    }

    private fun createAdapter() = CartsAdapter.getInstance(requireActivity().application).apply {
        listener = object : CartsAdapter.ClickListener {
            override fun onClick(position: Int) {
                navController.navigate(ViewCartListDirections.actionViewCartListToViewCart(carts[position]))
            }
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