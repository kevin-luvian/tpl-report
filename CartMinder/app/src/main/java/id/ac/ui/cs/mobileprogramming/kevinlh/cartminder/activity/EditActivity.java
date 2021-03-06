package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TimePicker;

import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.fragment.EditCartsFragment;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel.CartsViewModel;

public class EditActivity extends AppCompatActivity {

    public EditActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Carts");
    }

    @Override
    protected void onStart() {
        super.onStart();
        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.layout_fragment_container);
        layout.removeAllViewsInLayout();

        Fragment mainFragment = new EditCartsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_fragment_container, mainFragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}