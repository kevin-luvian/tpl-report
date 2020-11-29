package id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.model.NetworkInfo;

public class NetworkCardAdapter extends RecyclerView.Adapter<NetworkCardAdapter.ViewHolder> {
    private List<NetworkInfo> networks;

    public NetworkCardAdapter() {
        networks = new ArrayList<>();
    }

    public void setNetworks(List<NetworkInfo> networks) {
        if (networks != null) this.networks = networks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.w_info_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NetworkInfo ni = networks.get(position);
        holder.ssid.setText(ni.getSsid());
        holder.mac.setText(ni.getMac());
        holder.capabilities.setText(ni.getCapabilities());
        holder.channel.setText(String.format(Locale.ROOT, "channel %d", ni.getChannel()));
        holder.setImageBackground(ni.getLevel());
        holder.setScanLocation(ni.getScanLocation());
    }

    @Override
    public int getItemCount() {
        return networks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        CardView slWrapper;
        TextView scanLocation;
        TextView ssid;
        TextView mac;
        TextView capabilities;
        TextView channel;

        public ViewHolder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.network_image);
            slWrapper = view.findViewById(R.id.scan_location_wrapper);
            scanLocation = view.findViewById(R.id.network_scan_location);
            ssid = view.findViewById(R.id.network_ssid);
            mac = view.findViewById(R.id.network_mac);
            capabilities = view.findViewById(R.id.network_capabilities);
            channel = view.findViewById(R.id.network_channel);
        }

        void setImageBackground(int signalLevel) {
            int color = ContextCompat.getColor(image.getContext(), R.color.gray_shade);
            Drawable img = ContextCompat.getDrawable(image.getContext(), R.drawable.no_bar);
            switch (signalLevel) {
                case NetworkInfo.LEVEL_LOW:
                    color = ContextCompat.getColor(image.getContext(), R.color.red);
                    img = ContextCompat.getDrawable(image.getContext(), R.drawable.low_bar);
                    break;
                case NetworkInfo.LEVEL_MEDIUM:
                    color = ContextCompat.getColor(image.getContext(), R.color.yellow);
                    img = ContextCompat.getDrawable(image.getContext(), R.drawable.half_bar);
                    break;
                case NetworkInfo.LEVEL_HIGH:
                    color = ContextCompat.getColor(image.getContext(), R.color.light_green);
                    img = ContextCompat.getDrawable(image.getContext(), R.drawable.full_bar);
                    break;
            }
            image.setBackgroundColor(color);
            image.setImageDrawable(img);
        }

        void setScanLocation(int scanType) {
            int color = ContextCompat.getColor(image.getContext(), R.color.gray_shade);
            String text = scanLocation.getContext().getResources().getString(R.string.undefined_text);
            switch (scanType) {
                case NetworkInfo.FROM_LOCAL:
                    color = ContextCompat.getColor(image.getContext(), R.color.green);
                    text = scanLocation.getContext().getResources().getString(R.string.from_local);
                    break;
                case NetworkInfo.FROM_API:
                    color = ContextCompat.getColor(image.getContext(), R.color.blue);
                    text = scanLocation.getContext().getResources().getString(R.string.from_api);
                    break;
            }
            scanLocation.setText(text);
            slWrapper.setCardBackgroundColor(color);
        }
    }
}
