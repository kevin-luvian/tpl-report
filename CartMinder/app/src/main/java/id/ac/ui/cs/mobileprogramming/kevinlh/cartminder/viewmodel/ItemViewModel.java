package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.repository.ItemRepository;

public class ItemViewModel extends AndroidViewModel {
    private ItemRepository itemRepository;
    private Item item;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItemTitle(String itemTitle) {
        item.setTitle(itemTitle);
    }

    public void setItemDescription(String itemDescription) {
        item.setDescription(itemDescription);
    }

    public void setItemPrice(int itemPrice) {
        item.setPrice(itemPrice);
    }

    public void update() {
//        cartRepository.update(cart);
        System.out.println("Trying to update...");
    }
}