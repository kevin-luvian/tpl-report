package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.repository.CartRepository;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.repository.ItemRepository;

public class CartViewModel extends ViewModel {
    private CartRepository cartRepository;
    private ItemRepository itemRepository;
    private Cart cart;
    private MutableLiveData<List<Item>> liveItems;

    public CartViewModel(@NonNull Application application) {
        cartRepository = new CartRepository(application);
        itemRepository = new ItemRepository(application);
        cart = new Cart();
        liveItems = new MutableLiveData<>();
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        List<Item> cartItems = itemRepository.getCartItems(cart);
        if (cartItems == null) {
            cartItems = new ArrayList<Item>();
        }
        liveItems.setValue(cartItems);
    }

    public Cart getCart() {
        return cart;
    }

    public void setCartTitle(String title) {
        this.cart.setTitle(title);
    }

    public void setCartTime(int hour, int minute) {
        this.cart.setHour(hour);
        this.cart.setMinute(minute);
    }

    public LiveData<List<Item>> getCartItems() {
        return liveItems;
    }

    public void addCartItem(Item item) {
        liveItems.getValue().add(item);
//        liveItems.setValue(items);
        System.out.print("Live Items :");
        System.out.println(liveItems.getValue().toString());
    }

    public void removeCartItem(Item item) {
        liveItems.getValue().remove(item);
    }

    public void insert() {
        long cartId = cartRepository.insert(cart);
        if (cartId != -1) {
            itemRepository.insertAll(liveItems.getValue(), cartId);
        }
    }

    public void update() {
//        cartRepository.update(cart);
        System.out.println("Trying to update...");
    }
}