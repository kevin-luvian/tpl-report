package id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NetworksViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    private final Context context;

    public NetworksViewModelFactory(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NetworksViewModel.class) {
            return (T) new NetworksViewModel(context);
        } else {
            return null;
        }
    }
}