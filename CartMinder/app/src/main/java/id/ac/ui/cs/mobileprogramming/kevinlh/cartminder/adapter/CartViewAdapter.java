package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.adapter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.repository.ItemRepository;

public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.CartViewHolder> {
    private OnCartClickListener listener;
    private List<Cart> carts = new ArrayList<>();
    private final ItemRepository itemRepository;

    public CartViewAdapter(Application application) {
        itemRepository = new ItemRepository(application);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutID = R.layout.card_cart;
        View cartView = LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false);
        return new CartViewHolder(cartView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = carts.get(position);
        holder.tvCardTitle.setText(cart.getTitle());
        holder.tvCardTime.setText(cart.getTimeString());
        holder.tvCardTotalPrice.setText(calculateTotalPrice(cart));
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    @SuppressLint("DefaultLocale")
    private String calculateTotalPrice(Cart cart) {
        List<Item> items = itemRepository.getCartItems(cart);
        long result = 0;
        for (Item item : items) {
            result += item.getPrice();
        }
        return String.format(Locale.US, "%,d IDR", result);

    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
        notifyDataSetChanged();
    }

    public interface OnCartClickListener {
        void onClick(Cart cart);
    }

    public void setListener(OnCartClickListener listener) {
        this.listener = listener;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCardTitle;
        private TextView tvCardTime;
        private TextView tvCardTotalPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardTitle = itemView.findViewById(R.id.cart_title);
            tvCardTime = itemView.findViewById(R.id.cart_time);
            tvCardTotalPrice = itemView.findViewById(R.id.cart_total_price);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onClick(carts.get(position));
                }
            });
        }
    }
}
