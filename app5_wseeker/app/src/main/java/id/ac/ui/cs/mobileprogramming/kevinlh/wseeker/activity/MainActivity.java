package id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.adapter.NetworkCardAdapter;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.fragment.AppBarFragment;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.viewmodel.NetworksViewModel;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.viewmodel.NetworksViewModelFactory;

public class MainActivity extends AppCompatActivity {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    NetworksViewModel networksVM;
    CardView resetButton;
    ImageButton searchButton;
    ImageButton executeButton;
    RecyclerView networkCardsRV;
    NetworkCardAdapter networkCardsAD;

    /**
     * Native methods from the 'native-lib' native library,
     */
    private native String stringFromJNI();

    private native int addTwoInt(int a, int b);

    private native int generateRandomInt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworksViewModelFactory networksVMFactory = new NetworksViewModelFactory(this);
        networksVM = new ViewModelProvider(this, networksVMFactory).get(NetworksViewModel.class);
        resetButton = findViewById(R.id.button_reset);
        searchButton = findViewById(R.id.button_search);
        executeButton = findViewById(R.id.button_execute);
        networkCardsRV = findViewById(R.id.network_card_recyclerview);
        setFragments();
        setupRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        resetButton.setOnClickListener(v -> networksVM.resetNetworks());
        searchButton.setOnClickListener(v -> networksVM.scanNetworks());
        executeButton.setOnClickListener(v -> showTwoRandomIntAdded());
        networksVM.getLiveNetworks().observe(this, networks -> {
            networkCardsAD.setNetworks(networks);
        });
    }

    private void showTwoRandomIntAdded() {
        int numA = generateRandomInt();
        int numB = generateRandomInt();
        int numAPlusB = addTwoInt(numA, numB);
        String toastMessage = String.format(Locale.ROOT, "add using native\n%d + %d = %d", numA, numB, numAPlusB);
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
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