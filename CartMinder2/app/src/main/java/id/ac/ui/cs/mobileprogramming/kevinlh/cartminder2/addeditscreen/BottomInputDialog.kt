package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.viewmodel.AddEditViewModel
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.viewmodel.AddEditViewModelFactory

class BottomInputDialog : BottomSheetDialogFragment(), TextWatcher {
    private val TAG = "BottomInputDialog"
    private lateinit var addEditViewModel: AddEditViewModel
    private var iwdPos = -1
    private var tagHolder: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args: BottomInputDialogArgs by navArgs()
        iwdPos = args.IWDPos
        val factory = AddEditViewModelFactory(requireActivity().application)
        addEditViewModel = ViewModelProvider(requireActivity(), factory)
            .get(AddEditViewModel::class.java)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_input_dialog, container, false)
    }

    override fun onDestroy() {
//        Log.d(TAG, "fragment is destroyed")
        addEditViewModel.getIWD(iwdPos)?.detail?.apply {
            tags.add(tagHolder)
        }
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<EditText>(R.id.edit_tag).addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        tagHolder = s.toString()
    }

    override fun afterTextChanged(s: Editable?) {
    }
}