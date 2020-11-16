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
    private EditCartViewAdapter editCartViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartsViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(Objects.requireNonNull(getActivity()).getApplication()))
                .get(CartsViewModel.class);
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
        editCartViewAdapter = new EditCartViewAdapter();
        editCartViewAdapter.setListener(new CartClickListener());
        cartsRecyclerView.setAdapter(editCartViewAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        cartsViewModel.getCarts().observe(this, editCartViewAdapter::setCarts);
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