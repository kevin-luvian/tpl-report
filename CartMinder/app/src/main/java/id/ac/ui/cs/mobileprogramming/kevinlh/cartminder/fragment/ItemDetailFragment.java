package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.ItemDetail;

public class ItemDetailFragment extends Fragment {
    private static final int PERMISSION_STORAGE_CODE = 1;
    ItemDetail itemDetail;
    ImageView imageView;
    TextView tvCategory;
    TextView tvWeight;
    TextView tvDetail;

    public ItemDetailFragment() {
    }

    public ItemDetailFragment(ItemDetail itemDetail) {
        this.itemDetail = itemDetail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkStoragePermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);
        imageView = view.findViewById(R.id.image_view);
        tvCategory = view.findViewById(R.id.item_detail_category);
        tvWeight = view.findViewById(R.id.item_detail_weight);
        tvDetail = view.findViewById(R.id.item_detail_details);
        setupValues();
        return view;
    }

    private void setupValues() {
        try {
            Uri imageURI = Uri.parse(itemDetail.getImagePath());
            if (imageURI != null && !imageURI.equals(Uri.EMPTY))
                imageView.setImageURI(imageURI);
            tvCategory.setText(itemDetail.getCategory());
            tvWeight.setText(itemDetail.getWeight());
            tvDetail.setText(itemDetail.getDetail());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void checkStoragePermission() {
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
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupValues();
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}