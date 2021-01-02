package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.helper.SwipeCallback
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.helper.SwipeHelper
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.viewmodel.AddEditViewModel
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.viewmodel.AddEditViewModelFactory
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.enums.MarketCategory
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.helper.Utils
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen.adapter.ItemsAdapter
import java.util.*

class AddEditCart : Fragment(), View.OnClickListener {
    private var activityTitle: String = "Add Cart"
    private lateinit var navController: NavController
    private lateinit var addEditViewModel: AddEditViewModel
    private lateinit var etTitle: EditText
    private lateinit var spCategory: Spinner
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvTotalPrice: TextView
    private lateinit var rvItems: RecyclerView
    private lateinit var rvAdapter: ItemsAdapter

    private fun setActivityTitle() {
        (activity as AppCompatActivity?)?.supportActionBar?.title = activityTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val args: AddEditCartArgs by navArgs()
        val factory = AddEditViewModelFactory(requireActivity().application)
        addEditViewModel = ViewModelProvider(requireActivity(), factory)
            .get(AddEditViewModel::class.java)
        addEditViewModel.cart = args.cart
        args.cart?.apply {
            if (id > 0L) activityTitle = "Edit $title"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_menu) {
            Toast.makeText(requireContext(), "save button clicked", Toast.LENGTH_SHORT).show()
            addEditViewModel.saveData()
            navController.popBackStack()
            return true
        }
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        rvItems = view.findViewById(R.id.items_recycler_view)
        view.findViewById<Button>(R.id.btn_add).setOnClickListener(this)
        etTitle = view.findViewById(R.id.edit_title)
        spCategory = view.findViewById(R.id.edit_category)
        tvDate = view.findViewById(R.id.cart_date)
        tvTime = view.findViewById(R.id.cart_time)
        tvTotalPrice = view.findViewById(R.id.cart_total)
        setupRecyclerView()
        setupSpinner()
    }

    override fun onStart() {
        super.onStart()
        addEditViewModel.itemWithDetails.observe(viewLifecycleOwner, { mList ->
            rvAdapter.items = mList.map { it.item }
            var total = 0
            mList.forEach {
                total += it.item.price
            }
            val totalText = "${
                getString(R.string.currency)
            } ${
                Utils.numberToCurrencyFormat(total.toDouble())
            }"
            tvTotalPrice.text = totalText
        })
        addEditViewModel.cart?.let { cart ->
            etTitle.setText(cart.title)
            etTitle.addTextChangedListener(Watcher(0, cart))
            tvDate.text = Utils.calendarToDateString(cart.calendar)
            tvDate.setOnClickListener(this)
            tvTime.text = Utils.calendarToTimeString(cart.calendar)
            tvTime.setOnClickListener(this)
        }
    }

    override fun onResume() {
        super.onResume()
        addEditViewModel.cart?.apply {
            setActivityTitle()
        }
    }

    private fun setupSpinner() {
        spCategory.apply {
            onItemSelectedListener = createSpinnerItemListener()
            adapter = createSpinnerAdapter()
        }
        addEditViewModel.cart?.run {
            spCategory.setSelection(category.ordinal)
        }
    }

    private fun createSpinnerAdapter(): SpinnerAdapter {
        val categories = MarketCategory.values().map {
            it.name.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)
        }
        val dataAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, categories
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return dataAdapter
    }

    private fun createSpinnerItemListener(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val marketCategory =
                    parent?.getItemAtPosition(position).toString().toUpperCase(Locale.ROOT)
                addEditViewModel.cart?.category = MarketCategory.valueOf(marketCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) =
                Toast.makeText(parent?.context, "Selected: nothing", Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add -> navController.navigate(AddEditCartDirections.actionAddEditCartToAddEditItem())
            R.id.cart_date -> {
                addEditViewModel.cart?.calendar?.let { c ->
                    DatePickerDialog(
                        requireContext(),
                        { _, year, monthOfYear, dayOfMonth ->
                            c.apply {
                                set(Calendar.YEAR, year)
                                set(Calendar.MONTH, monthOfYear)
                                set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            }
                            tvDate.text = Utils.calendarToDateString(c)
                        }, c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.DAY_OF_MONTH]
                    ).show()
                }
            }
            R.id.cart_time -> {
                addEditViewModel.cart?.calendar?.let { c ->
                    TimePickerDialog(
                        requireContext(), { _, hourOfDay, minute ->
                            c.apply {
                                set(Calendar.HOUR_OF_DAY, hourOfDay)
                                set(Calendar.MINUTE, minute)
                            }
                            tvTime.text = Utils.calendarToTimeString(c)
                        }, c[Calendar.HOUR_OF_DAY], c[Calendar.MINUTE], true
                    ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addEditViewModel.clearData()
    }

    private fun setupRecyclerView() {
        rvAdapter = ItemsAdapter()
        rvItems.apply {
            layoutManager = createLayoutManager()
            adapter = rvAdapter
        }
        ItemTouchHelper(SwipeHelper(onClickCallback()))
            .attachToRecyclerView(rvItems)
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

    private fun onClickCallback() = object : SwipeCallback {
        override fun onDeleteClicked(position: Int) {
            addEditViewModel.removeItem(position)
            Toast.makeText(
                requireContext(),
                "delete button clicked on pos:$position",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onEditClicked(position: Int) {
            navController.navigate(AddEditCartDirections.actionAddEditCartToAddEditItem(position))
            Toast.makeText(
                requireContext(),
                "edit button clicked on pos:$position",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

class Watcher(private val watchType: Int, val cart: Cart) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val newText = s.toString()
        when (watchType) {
            0 -> cart.title = newText
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }
}