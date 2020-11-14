package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel.CartViewModel;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel.ItemViewModel;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodelfactory.CartViewModelFactory;

public class AddEditItemFragment extends Fragment {
    private boolean isEdit;
    private Item initialItem;
    private CartViewModel cartViewModel;
    private ItemViewModel itemViewModel;

    EditText editTitle;
    EditText editDescription;
    EditText editPrice;

    public AddEditItemFragment() {
        this(new Item(), false);
    }

    public AddEditItemFragment(Item item, boolean isEdit) {
        this.isEdit = isEdit;
        initialItem = item;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_item, container, false);
        editTitle = view.findViewById(R.id.edit_title);
        editDescription = view.findViewById(R.id.edit_description);
        editPrice = view.findViewById(R.id.edit_price);
        editTitle.addTextChangedListener(new EditTextWatcher("setTitle"));
        editDescription.addTextChangedListener(new EditTextWatcher("setDescription"));
        editPrice.addTextChangedListener(new EditTextWatcher("setPrice"));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CartViewModelFactory cartViewModelFactory = new CartViewModelFactory(getActivity().getApplication());
        cartViewModel = new ViewModelProvider(getActivity(), cartViewModelFactory).get(CartViewModel.class);
        itemViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(Objects.requireNonNull(getActivity()).getApplication()))
                .get(ItemViewModel.class);
        itemViewModel.setItem(initialItem);
        editTitle.setText(initialItem.getTitle());
        editDescription.setText(initialItem.getDescription());
        editPrice.setText(String.valueOf(initialItem.getPrice()));
        Toast.makeText(getActivity(), "itemID: " + initialItem.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                saveItem();
                return true;
            default:
                return false;
        }
    }

    private void saveItem() {
        Item currentItem = itemViewModel.getItem();
        if (currentItem.getTitle().trim().isEmpty()
                || currentItem.getDescription().trim().isEmpty()
                || currentItem.getPrice() < 0) {
            Toast.makeText(getActivity(), getString(R.string.warning_input_is_empty), Toast.LENGTH_SHORT).show();
        } else {
            if (!isEdit) cartViewModel.addCartItem(currentItem);
            Toast.makeText(getActivity(), "saving item", Toast.LENGTH_SHORT).show();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack("AddEditItemFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private class EditTextWatcher implements TextWatcher {
        String action;

        public EditTextWatcher(String action) {
            this.action = action;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (action) {
                case "setTitle":
                    itemViewModel.setItemTitle(s.toString());
                    break;
                case "setDescription":
                    itemViewModel.setItemDescription(s.toString());
                    break;
                case "setPrice":
                    int price;
                    if (s.toString().trim().isEmpty()) {
                        price = 0;
                    } else {
                        price = Integer.parseInt(s.toString());
                    }
                    itemViewModel.setItemPrice(price);
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