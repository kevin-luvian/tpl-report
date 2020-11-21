package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodelfactory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel.ItemViewModel;

public class ItemViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    private final Application application;

    public ItemViewModelFactory(@NonNull Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ItemViewModel.class) {
            return (T) new ItemViewModel(application);
        } else {
            return null;
        }
    }
}
