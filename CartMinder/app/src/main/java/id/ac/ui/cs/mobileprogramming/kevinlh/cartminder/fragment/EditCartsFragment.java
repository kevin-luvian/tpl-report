package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.adapter.EditCartViewAdapter;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel.CartsViewModel;

public class EditCartsFragment extends Fragment {
    private CartsViewModel cartsViewModel;
    private RecyclerView cartsRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_carts, container, false);

        FloatingActionButton buttonEdit = view.findViewById(R.id.carts_button_add);
        buttonEdit.setOnClickListener(v -> launchAddCartFragment());

        cartsRecyclerView = view.findViewById(R.id.carts_recyclerview);
        cartsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditCartViewAdapter editCartViewAdapter = new EditCartViewAdapter();
        editCartViewAdapter.setListener(new CartClickListener());
        cartsRecyclerView.setAdapter(editCartViewAdapter);

        cartsViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(Objects.requireNonNull(getActivity()).getApplication()))
                .get(CartsViewModel.class);
        cartsViewModel.getCarts().observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                editCartViewAdapter.setCarts(carts);
            }
        });
    }

    private void launchAddCartFragment() {
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.layout_fragment_container, new AddEditCartFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void launchEditCartFragment(Cart cart) {
        AddEditCartFragment addEditCartFragment = new AddEditCartFragment(cart, true);
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.layout_fragment_container, addEditCartFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private class CartClickListener implements EditCartViewAdapter.OnCartClickListener {
        @Override
        public void onEditClick(Cart cart) {
            launchEditCartFragment(cart);
        }

        @Override
        public void onDeleteClick(Cart cart) {
            cartsViewModel.delete(cart);
        }
    }
}