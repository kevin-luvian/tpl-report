package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.fragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.adapter.ItemViewAdapter;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel.CartViewModel;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodelfactory.CartViewModelFactory;

public class AddEditCartFragment extends Fragment {
    private boolean isEdit;
    private Cart initialCart;
    private CartViewModel cartViewModel;
    private RecyclerView itemsRecyclerView;
    private ItemViewAdapter itemViewAdapter;

    EditText editTitle;
    EditText editTime;

    public AddEditCartFragment() {
        this(new Cart(), false);
    }

    public AddEditCartFragment(Cart cart, boolean isEdit) {
        this.isEdit = isEdit;
        initialCart = cart;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        CartViewModelFactory cartViewModelFactory = new CartViewModelFactory(getActivity().getApplication());
        cartViewModel = new ViewModelProvider(getActivity(), cartViewModelFactory).get(CartViewModel.class);
        cartViewModel.setCart(initialCart);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_cart, container, false);
        FloatingActionButton buttonEdit = view.findViewById(R.id.items_button_add);
        buttonEdit.setOnClickListener(v -> launchAddItemFragment());
        editTitle = view.findViewById(R.id.edit_title);
        editTime = view.findViewById(R.id.edit_time);
        editTitle.addTextChangedListener(new EditTextWatcher("setTitle"));
        editTime.addTextChangedListener(new EditTextWatcher("setTime"));

        cartViewModel.getCartItems().observe(getViewLifecycleOwner(), this::onItemsUpdate);

        itemsRecyclerView = view.findViewById(R.id.items_recyclerview);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        itemViewAdapter = new ItemViewAdapter();
//        itemViewAdapter.setItems(cartViewModel.getCartItems().getValue());
        itemViewAdapter.setListener(new AddEditCartFragment.ItemClickListener());
        itemsRecyclerView.setAdapter(itemViewAdapter);
//        if (getArguments() != null) {
//            Item newItem = (Item) getArguments().getParcelable("Item");
//            cartViewModel.addCartItem(newItem);
//        }

        editTitle.setText(initialCart.getTitle());
        editTime.setText(initialCart.getTime());
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), "cartID: " + initialCart.getId(), Toast.LENGTH_SHORT).show();
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
                saveCart();
                return true;
            default:
                return false;
        }
    }

    private void onItemsUpdate(List<Item> items) {
        System.out.println("Items");
        System.out.println(items.toString());
        itemViewAdapter.setItems(items);
    }

    private void launchAddItemFragment() {
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.layout_fragment_container, new AddEditItemFragment());
        transaction.addToBackStack("AddEditItemFragment");
        transaction.commit();
    }

    private void saveCart() {
        Cart currentCart = cartViewModel.getCart();
        if (currentCart.getTitle().trim().isEmpty() || currentCart.getTime().trim().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.warning_input_is_empty), Toast.LENGTH_SHORT).show();
        } else {
            if (isEdit) cartViewModel.update();
            else cartViewModel.insert();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
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
                    cartViewModel.setCartTitle(s.toString());
                    break;
                case "setTime":
                    cartViewModel.setCartTime(s.toString());
                    break;
                default:
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private class ItemClickListener implements ItemViewAdapter.OnClickListener {
        @Override
        public void onClick(Item item) {
            System.out.println("Item clicked");
        }
    }
}