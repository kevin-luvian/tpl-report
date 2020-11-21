package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.ItemDetail;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel.ItemViewModel;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodelfactory.ItemViewModelFactory;

import static android.app.Activity.RESULT_OK;

public class AddEditItemDetailFragment extends Fragment {
    private static final int INTENT_IMAGE_CODE = 17000;
    private static final int PERMISSION_STORAGE_CODE = 1;
    private ItemViewModel itemViewModel;
    //    private String imageFilePath = "";
    ImageView imageView;
    Button buttonChooseImage;
    EditText editCategory;
    EditText editWeight;
    EditText editDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ItemViewModelFactory itemViewModelFactory = new ItemViewModelFactory(Objects.requireNonNull(getActivity()).getApplication());
        itemViewModel = new ViewModelProvider(getActivity(), itemViewModelFactory).get(ItemViewModel.class);
        itemViewModel.initializeItemDetail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_item_detail, container, false);
        imageView = view.findViewById(R.id.image_view);
        buttonChooseImage = view.findViewById(R.id.button_choose_image);
        editCategory = view.findViewById(R.id.edit_category);
        editWeight = view.findViewById(R.id.edit_weight);
        editDetail = view.findViewById(R.id.edit_detail);
        setupViewValues();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buttonChooseImage.setOnClickListener(v -> chooseGalleryImage());
        editCategory.addTextChangedListener(new EditTextWatcher(1));
        editWeight.addTextChangedListener(new EditTextWatcher(2));
        editDetail.addTextChangedListener(new EditTextWatcher(3));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_menu) {
            saveItemDetail();
            return true;
        }
        return false;
    }

//    private void openCamera() {
//        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (pictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
//            startActivityForResult(pictureIntent, INTENT_IMAGE_CODE); //change this
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == INTENT_IMAGE_CODE &&
//                resultCode == RESULT_OK) {
//            if (data != null && data.getExtras() != null) {
//                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
//                imageView.setImageBitmap(imageBitmap);
//            }
//        }
//    }
//
//    private File createImageFile() throws IOException {
//        String imageFileName = "IMG_" + UUID.randomUUID().toString();
//        File storageDir = Objects.requireNonNull(getActivity()).
//                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        imageFilePath = image.getAbsolutePath();
//        return image;
//    }

    private void chooseGalleryImage() {
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Requesting permission")
                        .setMessage("Permission is needed to access and show images in the gallery")
                        .setPositiveButton("ok", (dialog, which) ->
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_STORAGE_CODE))
                        .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, INTENT_IMAGE_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == INTENT_IMAGE_CODE) {
            if (data == null)
                Toast.makeText(getActivity(), "no image selected", Toast.LENGTH_SHORT).show();
            else {
                Uri imageURI = data.getData();
                imageView.setImageURI(imageURI);
                itemViewModel.getItemDetail().setImagePath(imageURI.toString());
                System.out.println("Image URI : " + imageURI.toString());
            }
        }
    }

    private void setupViewValues() {
        ItemDetail itemDetail = itemViewModel.getItemDetail();
        Uri imageURI = Uri.parse(itemDetail.getImagePath());
        if (imageURI != null && !imageURI.equals(Uri.EMPTY))
            imageView.setImageURI(imageURI);
        editCategory.setText(itemDetail.getCategory());
        editWeight.setText(itemDetail.getWeight());
        editDetail.setText(itemDetail.getDetail());
    }

    private void saveItemDetail() {
        ItemDetail itemDetail = itemViewModel.getItemDetail();
        if (itemDetail.getCategory().trim().isEmpty()
                || itemDetail.getWeight().trim().isEmpty()
                || itemDetail.getDetail().trim().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.warning_input_is_empty), Toast.LENGTH_SHORT).show();
        } else {
            itemViewModel.saveDetail();
            Toast.makeText(getActivity(), "saving item detail", Toast.LENGTH_SHORT).show();
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fm.popBackStack("AddEditItemDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private class EditTextWatcher implements TextWatcher {
        private final int operation;

        public EditTextWatcher(int operation) {
            this.operation = operation;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            ItemDetail itemDetail = itemViewModel.getItemDetail();
            switch (operation) {
                case 1:
                    itemDetail.setCategory(s.toString());
                    break;
                case 2:
                    itemDetail.setWeight(s.toString());
                    break;
                case 3:
                    itemDetail.setDetail(s.toString());
                    break;
                default:
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}