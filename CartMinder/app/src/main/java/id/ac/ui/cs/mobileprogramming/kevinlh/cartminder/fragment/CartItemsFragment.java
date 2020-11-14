package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.adapter.EditCartViewAdapter;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.adapter.ItemViewAdapter;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel.CartViewModel;

public class CartItemsFragment extends Fragment {
    private Cart initialCart;
    private CartViewModel cartViewModel;
    private RecyclerView itemsRecyclerView;

    TextView cartTitle;
    TextView cartTime;

    public CartItemsFragment(Cart cart) {
        initialCart = cart;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_items, container, false);
        cartTitle = view.findViewById(R.id.cart_title);
        cartTime = view.findViewById(R.id.cart_time);

        itemsRecyclerView = view.findViewById(R.id.items_recyclerview);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cartViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(Objects.requireNonNull(getActivity()).getApplication()))
                .get(CartViewModel.class);
        cartViewModel.setCart(initialCart);
        cartTitle.setText(initialCart.getTitle());
        cartTime.setText(initialCart.getTime());

        ItemViewAdapter itemViewAdapter = new ItemViewAdapter();
        itemViewAdapter.setListener(new ItemClickListener());
        itemsRecyclerView.setAdapter(itemViewAdapter);

        cartViewModel.getCartItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                System.out.println("Items");
                System.out.println(items.toString());
                itemViewAdapter.setItems(items);
            }
        });
    }

    private class ItemClickListener implements ItemViewAdapter.OnClickListener {
        @Override
        public void onClick(Item item) {
            System.out.println("Item clicked");
        }
    }
}