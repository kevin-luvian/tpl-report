package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.adapter.ItemViewAdapter;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel.CartViewModel;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodelfactory.CartViewModelFactory;

public class CartFragment extends Fragment {
    private Cart initialCart;
    private CartViewModel cartViewModel;
    private RecyclerView itemsRecyclerView;

    TextView cartTitle;
    TextView cartTime;

    public CartFragment(Cart cart) {
        initialCart = cart;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CartViewModelFactory cartViewModelFactory = new CartViewModelFactory(getActivity().getApplication());
        cartViewModel = new ViewModelProvider(getActivity(), cartViewModelFactory).get(CartViewModel.class);
        cartViewModel.setCart(initialCart);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cartTitle = view.findViewById(R.id.cart_title);
        cartTime = view.findViewById(R.id.cart_time);
        cartTitle.setText(initialCart.getTitle());
        cartTime.setText(initialCart.getTimeString());

        itemsRecyclerView = view.findViewById(R.id.items_recyclerview);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ItemViewAdapter itemViewAdapter = new ItemViewAdapter();
        itemViewAdapter.setListener(new ItemClickListener());
        itemsRecyclerView.setAdapter(itemViewAdapter);
        cartViewModel.getCartItems().observe(this, items -> {
            System.out.println("Items");
            System.out.println(items.toString());
            itemViewAdapter.setItems(items);
        });
    }

    private class ItemClickListener implements ItemViewAdapter.OnClickListener {
        @Override
        public void onClick(Item item) {
            System.out.println("Item clicked");
        }
    }
}