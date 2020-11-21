package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.adapter.ItemViewAdapter;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.ItemDetail;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.repository.ItemRepository;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel.CartViewModel;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodelfactory.CartViewModelFactory;

public class CartFragment extends Fragment {
    private Cart initialCart;
    private CartViewModel cartViewModel;
    private RecyclerView itemsRecyclerView;
    private ItemRepository itemRepository;

    TextView cartTitle;
    TextView cartTime;
    TextView cartTotalPrice;

    public CartFragment() {
    }

    public CartFragment(Cart cart) {
        initialCart = cart;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemRepository = new ItemRepository(getActivity().getApplication());
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
        cartTotalPrice = view.findViewById(R.id.cart_total_items_price);

        itemsRecyclerView = view.findViewById(R.id.items_recyclerview);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setupValues();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ItemViewAdapter itemViewAdapter = new ItemViewAdapter();
        itemViewAdapter.setListener(this::launchItemDetailFragment);
        itemsRecyclerView.setAdapter(itemViewAdapter);
        cartViewModel.getCartItems().observe(this, itemViewAdapter::setItems);
    }

    private void setupValues() {
        if (initialCart == null) return;
        cartTitle.setText(initialCart.getTitle());
        cartTime.setText(initialCart.getTimeString());
        cartTotalPrice.setText(String.format(Locale.US, "%,d IDR", cartViewModel.getTotalPrice()));
    }

    private void launchItemDetailFragment(Item item) {
        ItemDetail itemDetail = itemRepository.getItemDetail(item);
        if (itemDetail != null) {
            FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
            transaction.replace(R.id.layout_fragment_container, new ItemDetailFragment(itemDetail));
            transaction.addToBackStack("ItemDetailFragment");
            transaction.commit();
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.toast_message_no_detail), Toast.LENGTH_SHORT).show();
        }
    }
}