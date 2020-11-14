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
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;

public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.CartViewHolder> {
    private OnClickListener listener;
    private List<Item> items = new ArrayList<>();

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutID = R.layout.card_item;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutID, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Item item = items.get(position);
        holder.tvItemTitle.setText(item.getTitle());
        holder.tvItemDescription.setText(item.getDescription());
        holder.tvItemPrice.setText(String.valueOf(item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> items) {
        this.items = items;
//        notifyDataSetChanged(); // Replace this
    }

    public interface OnClickListener {
        void onClick(Item item);
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle;
        private TextView tvItemDescription;
        private TextView tvItemPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.item_title);
            tvItemDescription = itemView.findViewById(R.id.item_description);
            tvItemPrice = itemView.findViewById(R.id.item_price);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onClick(items.get(position));
                }
            });
        }
    }
}
