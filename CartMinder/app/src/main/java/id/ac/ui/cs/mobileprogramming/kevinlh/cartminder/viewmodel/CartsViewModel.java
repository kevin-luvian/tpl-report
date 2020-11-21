package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.repository.CartRepository;

public class CartsViewModel extends AndroidViewModel {
    private CartRepository cartRepository;
    private LiveData<List<Cart>> carts;

    public CartsViewModel(@NonNull Application application) {
        super(application);
        cartRepository = new CartRepository(application);
        carts = cartRepository.getCarts();
    }

    public void insert(Cart cart) {
        cartRepository.insert(cart);
    }

    public void update(Cart cart) {
        cartRepository.update(cart);
    }

    public void delete(Cart cart) {
        cartRepository.delete(cart);
    }

    public LiveData<List<Cart>> getCarts() {
        return carts;
    }
}
