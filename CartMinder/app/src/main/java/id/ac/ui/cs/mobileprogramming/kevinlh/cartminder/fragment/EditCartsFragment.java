package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.adapter.EditCartViewAdapter;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.utils.AlarmReceiver;
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
        cartsRecyclerView.setLayoutManager(getLayoutManager());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editCartViewAdapter = new EditCartViewAdapter();
        editCartViewAdapter.setListener(new CartClickListener());
        cartsRecyclerView.setAdapter(editCartViewAdapter);
        cartsViewModel.getCarts().observe(this, carts -> {
            editCartViewAdapter.setCarts(carts);
            setAlarms(carts);
        });
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return new GridLayoutManager(getContext(), 2);
        return new LinearLayoutManager(getContext());
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

    private void setAlarms(List<Cart> carts) {
        for (Cart cart : carts) {
            Calendar cal = cart.getTimeAsCalendar();
            startAlarm(cal, cart);
        }
    }

    private void startAlarm(Calendar cal, Cart cart) {
        if (System.currentTimeMillis() > cal.getTimeInMillis()) return;
        int id = (int) cart.getId();
        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("cartId", id);
        intent.putExtra("cartTitle", cart.getTitle());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
//        Toast.makeText(getContext(),
//                getResources().getString(R.string.toast_message_alarm_prefix) + " " + cart.getTimeString(),
//                Toast.LENGTH_SHORT).show();
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