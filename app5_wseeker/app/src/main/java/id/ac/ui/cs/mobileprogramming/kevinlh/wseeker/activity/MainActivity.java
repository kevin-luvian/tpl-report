package id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.adapter.NetworkCardAdapter;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.fragment.AppBarFragment;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.helper.NetworkScanHelper;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.viewmodel.NetworksViewModel;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.viewmodel.NetworksViewModelFactory;

public class MainActivity extends AppCompatActivity {
    NetworksViewModel networksVM;
    CardView resetButton;
    ImageButton searchButton;
    RecyclerView networkCardsRV;
    NetworkCardAdapter networkCardsAD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworksViewModelFactory networksVMFactory = new NetworksViewModelFactory(this);
        networksVM = new ViewModelProvider(this, networksVMFactory).get(NetworksViewModel.class);
        resetButton = findViewById(R.id.button_reset);
        searchButton = findViewById(R.id.button_search);
        networkCardsRV = findViewById(R.id.network_card_recyclerview);
        setFragments();
        setupRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        resetButton.setOnClickListener(v -> {
            networksVM.resetNetworks();
        });
        searchButton.setOnClickListener(v -> {
            networksVM.scanNetworks();
        });
        networksVM.getLiveNetworks().observe(this, networks -> {
            networkCardsAD.setNetworks(networks);
        });
    }

    private void setFragments() {
        AppBarFragment fragmentTop = new AppBarFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.top_bar_container, fragmentTop);
        transaction.commit();
    }

    private void setupRecyclerView() {
        networkCardsRV.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        networkCardsAD = new NetworkCardAdapter();
        networkCardsRV.setAdapter(networkCardsAD);
    }
}