package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;

public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.CartViewHolder> {
    private OnCartClickListener listener;
    private List<Cart> carts = new ArrayList<>();

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
        holder.tvCardTime.setText(cart.getTime());
        holder.tvCardTotalPrice.setText("Rp. 50.000");
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
        notifyDataSetChanged(); // Replace this
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
