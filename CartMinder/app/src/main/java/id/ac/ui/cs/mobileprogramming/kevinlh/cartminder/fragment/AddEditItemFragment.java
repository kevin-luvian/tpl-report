package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.Toast;

import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel.CartViewModel;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel.ItemViewModel;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodelfactory.CartViewModelFactory;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodelfactory.ItemViewModelFactory;

public class AddEditItemFragment extends Fragment {
    private boolean isEdit;
    private Item initialItem;
    private CartViewModel cartViewModel;
    private ItemViewModel itemViewModel;

    EditText editTitle;
    EditText editDescription;
    EditText editPrice;
    Button buttonDetail;

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
        CartViewModelFactory cartViewModelFactory = new CartViewModelFactory(Objects.requireNonNull(getActivity()).getApplication());
        cartViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()), cartViewModelFactory).get(CartViewModel.class);
        ItemViewModelFactory itemViewModelFactory = new ItemViewModelFactory(getActivity().getApplication());
        itemViewModel = new ViewModelProvider(getActivity(), itemViewModelFactory).get(ItemViewModel.class);
        itemViewModel.setItem(initialItem);
        if (isEdit) {
            int position = cartViewModel.getCartItemPosition(initialItem);
            itemViewModel.setPosition(position);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_item, container, false);
        editTitle = view.findViewById(R.id.edit_title);
        editDescription = view.findViewById(R.id.edit_description);
        editPrice = view.findViewById(R.id.edit_price);
        buttonDetail = view.findViewById(R.id.item_button_detail);
        editTitle.addTextChangedListener(new EditTextWatcher("setTitle"));
        editDescription.addTextChangedListener(new EditTextWatcher("setDescription"));
        editPrice.addTextChangedListener(new EditTextWatcher("setPrice"));
        if (isEdit) {
            buttonDetail.setOnClickListener(v -> launchEditItemDetailFragment());
        } else {
            buttonDetail.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Item item = itemViewModel.getItem();
        editTitle.setText(item.getTitle());
        editDescription.setText(item.getDescription());
        editPrice.setText(String.valueOf(item.getPrice()));
//        Toast.makeText(getActivity(), "itemID: " + item.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_menu) {
            saveItem();
            return true;
        }
        return false;
    }

    private void launchEditItemDetailFragment() {
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.layout_fragment_container, new AddEditItemDetailFragment());
        transaction.addToBackStack("AddEditItemDetailFragment");
        transaction.commit();
    }

    private void saveItem() {
        Item currentItem = itemViewModel.getItem();
        if (currentItem.getTitle().trim().isEmpty()
                || currentItem.getDescription().trim().isEmpty()
                || currentItem.getPrice() < 0) {
            Toast.makeText(getActivity(), getString(R.string.warning_input_is_empty), Toast.LENGTH_SHORT).show();
        } else {
            if (isEdit) {
                cartViewModel.replaceCartItem(itemViewModel.getPosition(), currentItem);
                itemViewModel.saveItem();
            } else {
                cartViewModel.addCartItem(currentItem);
            }
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
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
            Item item = itemViewModel.getItem();
            switch (action) {
                case "setTitle":
                    item.setTitle(s.toString());
                    break;
                case "setDescription":
                    item.setDescription(s.toString());
                    break;
                case "setPrice":
                    int price;
                    if (s.toString().trim().isEmpty()) price = 0;
                    else price = Integer.parseInt(s.toString());
                    item.setPrice(price);
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