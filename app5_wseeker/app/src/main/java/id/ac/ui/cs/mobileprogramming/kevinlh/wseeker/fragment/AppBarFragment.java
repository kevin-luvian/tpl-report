package id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.R;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.viewmodel.NetworksViewModel;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.viewmodel.NetworksViewModelFactory;

public class AppBarFragment extends Fragment {
    NetworksViewModel networksVM;
    FrameLayout optionWrapper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworksViewModelFactory networksVMFactory = new NetworksViewModelFactory(getActivity());
        networksVM = new ViewModelProvider(getActivity(), networksVMFactory).get(NetworksViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_app_bar, container, false);
        optionWrapper = view.findViewById(R.id.option_menu_wrapper);
        optionWrapper.setOnClickListener(this::showPopupMenu);
        return view;
    }

    public void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_upload:
                    networksVM.uploadNetworks();
                    Toast.makeText(getActivity(), "uploading...", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menu_fetch:
                    networksVM.fetchNetworks();
                    Toast.makeText(getActivity(), "fetching...", Toast.LENGTH_LONG).show();
                    break;
                case R.id.menu_delete:
                    networksVM.deleteNetworks();
                    Toast.makeText(getActivity(), "deleting...", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });
        popupMenu.inflate(R.menu.popup);
        popupMenu.show();
    }
}
