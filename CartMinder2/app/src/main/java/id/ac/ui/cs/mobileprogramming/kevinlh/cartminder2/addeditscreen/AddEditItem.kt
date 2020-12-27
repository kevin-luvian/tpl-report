package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.viewmodel.AddEditViewModel
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.viewmodel.AddEditViewModelFactory
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Item

class AddEditItem : Fragment() {
    private var activityTitle: String = "Add Item"
    private var iwdPos: Int = -1
    private lateinit var navController: NavController
    private lateinit var addEditViewModel: AddEditViewModel
    private lateinit var etTitle: EditText
    private lateinit var etPrice: EditText
    private lateinit var btnItemDetail: Button

    private fun setActivityTitle() {
        (activity as AppCompatActivity?)?.supportActionBar?.title = activityTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args: AddEditItemArgs by navArgs()
        val factory = AddEditViewModelFactory(requireActivity().application)
        addEditViewModel = ViewModelProvider(requireActivity(), factory)
            .get(AddEditViewModel::class.java)
        createAcivityTitle(args.IWDPos)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        etTitle = view.findViewById(R.id.edit_title)
        etPrice = view.findViewById(R.id.edit_price)
        btnItemDetail = view.findViewById(R.id.btn_item_detail)
    }

    override fun onStart() {
        super.onStart()
        btnItemDetail.setOnClickListener {
            navController.navigate(AddEditItemDirections.actionAddEditItemToAddEditDetail(iwdPos))
        }
        addEditViewModel.getIWD(iwdPos)?.apply {
            etTitle.addTextChangedListener(Watcher(0, item))
            etPrice.addTextChangedListener(Watcher(1, item))
            item.run {
                etTitle.setText(title)
                etPrice.setText(price.toString())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setActivityTitle()
        checkBtnDetailText()
    }

    private fun checkBtnDetailText() {
        addEditViewModel.getIWD(iwdPos)?.detail.let {
            if (it == null) btnItemDetail.text = resources.getString(R.string.create_details)
            else btnItemDetail.text = resources.getString(R.string.edit_details)
        }
    }

    private fun createAcivityTitle(pos: Int) {
        if (pos > -1) {
            iwdPos = pos
            addEditViewModel.getIWD(pos)?.item?.apply {
                activityTitle = if (title.isNotBlank()) "Edit $title" else "Edit Item"
            }
        } else {
            iwdPos = addEditViewModel.createNewItem()
        }
    }

    class Watcher(private val watchType: Int, private val item: Item) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (watchType) {
                0 -> item.title = s.toString()
                1 -> item.price = if (s.toString().trim().isEmpty()) 0 else s.toString().toInt()
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }
}