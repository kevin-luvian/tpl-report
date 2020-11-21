package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodelfactory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel.CartViewModel;

public class CartViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    private final Application application;

    public CartViewModelFactory(@NonNull Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == CartViewModel.class) {
            return (T) new CartViewModel(application);
        } else {
            return null;
        }
    }
}
