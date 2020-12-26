package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.adapter.EditTagsAdapter
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.helper.ImagePickerHelper
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.viewmodel.AddEditViewModel
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.viewmodel.AddEditViewModelFactory
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.generalhelper.FileHelper
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.generalhelper.Utils
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Detail
import java.io.File


class AddEditDetail : Fragment() {
    companion object {
        const val REQUEST_IMAGE_CAPTURE_CODE = 1
        const val REQUEST_IMAGE_GALLERY_CODE = 2
    }

    private var activityTitle: String = "Add Detail"
    private var iwdPos: Int = -1
    private lateinit var navController: NavController
    private lateinit var addEditViewModel: AddEditViewModel
    private lateinit var rvTags: RecyclerView
    private lateinit var rvAdapter: EditTagsAdapter
    private lateinit var etDescription: EditText
    private lateinit var ivImage: ImageView
    private lateinit var btnChooseImage: Button
    private lateinit var btnAddTag: Button
    private lateinit var imagePathHolder: String

    private fun setActivityTitle() {
        (activity as AppCompatActivity?)?.supportActionBar?.title = activityTitle
    }

    private fun createAcivityTitle() {
        addEditViewModel.getIWD(iwdPos)?.detail.let {
            if (it == null) addEditViewModel.createNewDetail(iwdPos)
            else activityTitle = "Edit Detail"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val args: AddEditItemArgs by navArgs()
        val factory = AddEditViewModelFactory(requireActivity().application)
        addEditViewModel = ViewModelProvider(requireActivity(), factory)
            .get(AddEditViewModel::class.java)
        iwdPos = args.IWDPos
        createAcivityTitle()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Toast.makeText(requireContext(), "delete button clicked", Toast.LENGTH_SHORT).show()
        addEditViewModel.removeDetail(iwdPos)
        navController.popBackStack()
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        rvTags = view.findViewById(R.id.tags_recycler_view)
        etDescription = view.findViewById(R.id.edit_description)
        ivImage = view.findViewById(R.id.detail_image)
        btnChooseImage = view.findViewById(R.id.btn_choose_image)
        btnAddTag = view.findViewById(R.id.btn_add_tag)
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        addEditViewModel.getIWD(iwdPos)?.detail?.let { detail ->
            Log.d(AddEditDetail::class.toString(), "Detail :: $detail")
            addEditViewModel.setLiveTagHolder(detail.tags)
            etDescription.setText(detail.description)
            etDescription.addTextChangedListener(Watcher(detail))
            detail.imagePath?.let { path ->
                Log.d("Image", "img path:$path")
                val file = File(path)
                if (file.exists()) {
                    val uri = FileHelper.createImageUri(requireContext(), file)
                    ivImage.setImageURI(uri)
                }
            }
        }
        btnChooseImage.setOnClickListener {
            ImagePickerHelper.showImagePickerOptions(requireActivity(), imagePickerListener())
        }
        btnAddTag.setOnClickListener {
            navController.navigate(
                AddEditDetailDirections.actionAddEditDetailToBottomInputDialog(iwdPos)
            )
        }
        addEditViewModel.liveTagsHolder.observe(viewLifecycleOwner, {
            rvAdapter.tags = it
        })
    }

    override fun onResume() {
        super.onResume()
        setActivityTitle()
    }

    private fun imagePickerListener() = object : ImagePickerHelper.PickerOptionListener {
        override fun onTakeCameraSelected() = launchCameraIntent()
        override fun onChooseGallerySelected() = launchGalleryIntent()
    }

    private fun launchCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            val photoFile: File =
                FileHelper.createImageFile(requireContext(), Utils.getRandomString(10))
            imagePathHolder = photoFile.absolutePath
            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                "id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.fileProvider",
                photoFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_CODE)
        }
    }

    private fun launchGalleryIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE_CODE -> if (resultCode == Activity.RESULT_OK) {
                val imageURI = Uri.fromFile(File(imagePathHolder))
                ivImage.setImageURI(imageURI)
                updateDetailImagePath(imagePathHolder)
            }
            REQUEST_IMAGE_GALLERY_CODE -> if (resultCode == Activity.RESULT_OK) {
//                val filename: String
                data?.data?.let { uri ->
                    ivImage.setImageURI(uri)
                    FileHelper.getPathFromUri(requireContext(), uri)?.let { path ->
                        updateDetailImagePath(path)
                    }
                }
            }
        }
    }

    private fun updateDetailImagePath(path: String) {
        addEditViewModel.getIWD(iwdPos)?.detail?.apply {
            imagePath = path
        }
    }

//    private fun launchTakePicture() {
//        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
//            if (success) {
////                updateImage(imageUriHolder)
//                ivImage.setImageURI(imageUriHolder)
//            }
//        }.launch(imageUriHolder)
//    }
//
//    private fun launchGallery() {
//        registerForActivityResult(ActivityResultContracts.GetContent()) {
//            // Handle the returned Uri
//            ivImage.setImageURI(it)
//        }.launch("image/*")
//    }

    private fun setupRecyclerView() {
        rvAdapter = EditTagsAdapter(tagsAdapterListener())
        rvTags.apply {
            layoutManager = linearLayoutManager()
            adapter = rvAdapter
        }
    }

    private fun tagsAdapterListener() = object : EditTagsAdapter.Listener {
        override fun onDeleteClick(position: Int) {
            addEditViewModel.getIWD(iwdPos)?.detail?.run {
                tags.removeAt(position)
                addEditViewModel.setLiveTagHolder(tags.toMutableList())
            }
        }
    }

    private fun linearLayoutManager() = object : LinearLayoutManager(context) {
        override fun setOrientation(orientation: Int) = super.setOrientation(HORIZONTAL)

        override fun setInitialPrefetchItemCount(itemCount: Int) =
            super.setInitialPrefetchItemCount(4)
    }

    private class Watcher(private val detail: Detail) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            detail.description = s.toString()
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }
}