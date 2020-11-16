package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.fragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import android.widget.TimePicker;
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

public class AddEditCartFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {
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
        editTitle.setText(initialCart.getTitle());
        editTitle.addTextChangedListener(new EditTextWatcher());
        editTime = view.findViewById(R.id.edit_time);
        editTime.setText(initialCart.getTimeString());
        TimePickerDialog.OnTimeSetListener listener = this;
        editTime.setOnClickListener(v -> {
            Cart cart = cartViewModel.getCart();
            TimePickerFragment timePickerFragment = new TimePickerFragment(cart.getHour(), cart.getMinute());
            timePickerFragment.setListener(listener);
            timePickerFragment.show(getFragmentManager(), "timePicker");
        });

        itemsRecyclerView = view.findViewById(R.id.items_recyclerview);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        itemViewAdapter = new ItemViewAdapter();
        itemViewAdapter.setListener(this::launchEditItemFragment);
        itemsRecyclerView.setAdapter(itemViewAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.ACTION_STATE_IDLE,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                cartViewModel.removeCartItem(itemViewAdapter.getItemAt(position));
                itemViewAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(itemsRecyclerView);
    }

    @Override
    public void onStart() {
        super.onStart();
        cartViewModel.getCartItems().observe(getViewLifecycleOwner(), this::onItemsUpdate);
        Toast.makeText(getActivity(), "cartID: " + initialCart.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Resuming");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_menu) {
            saveCart();
            return true;
        }
        return false;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        cartViewModel.setCartTime(hourOfDay, minute);
        editTime.setText(cartViewModel.getCart().getTimeString());
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

    private void launchEditItemFragment(Item item) {
        AddEditItemFragment addEditItemFragment = new AddEditItemFragment(item, true);
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.layout_fragment_container, addEditItemFragment);
        transaction.addToBackStack("AddEditItemFragment");
        transaction.commit();
    }

    private void saveCart() {
        Cart currentCart = cartViewModel.getCart();
        if (currentCart.getTitle().trim().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.warning_input_is_empty), Toast.LENGTH_SHORT).show();
        } else {
            if (isEdit) cartViewModel.update();
            else cartViewModel.insert();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        }
    }

    private class EditTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            cartViewModel.setCartTitle(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}