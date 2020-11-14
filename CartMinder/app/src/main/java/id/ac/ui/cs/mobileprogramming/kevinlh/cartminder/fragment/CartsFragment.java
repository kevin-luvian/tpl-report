package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.activity.EditActivity;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.adapter.CartViewAdapter;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel.CartsViewModel;

public class CartsFragment extends Fragment {
    private CartsViewModel cartsViewModel;
    private RecyclerView cartsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carts, container, false);

        FloatingActionButton buttonEdit = view.findViewById(R.id.carts_button_edit);
        buttonEdit.setOnClickListener(v -> launchEditActivity());

        cartsRecyclerView = view.findViewById(R.id.carts_recyclerview);
        cartsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CartViewAdapter cartViewAdapter = new CartViewAdapter();
        cartViewAdapter.setListener(new CartClickListener());
        cartsRecyclerView.setAdapter(cartViewAdapter);

        cartsViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(Objects.requireNonNull(getActivity()).getApplication()))
                .get(CartsViewModel.class);
        cartsViewModel.getCarts().observe(this, cartViewAdapter::setCarts);
    }

    private void launchEditActivity() {
        Intent intent = new Intent(getActivity(), EditActivity.class);
        startActivity(intent);
    }

    private void launchCartItemsFragment(Cart cart) {
        CartItemsFragment cartItemsFragment = new CartItemsFragment(cart);
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.layout_fragment_container, cartItemsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private class CartClickListener implements CartViewAdapter.OnCartClickListener {
        @Override
        public void onClick(Cart cart) {
            launchCartItemsFragment(cart);
        }
    }
}