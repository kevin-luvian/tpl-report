package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.ItemDetail;

public class ItemViewModel extends AndroidViewModel {
    private Item item;
    private ItemDetail itemDetail;
    private int position;

    public ItemViewModel(@NonNull Application application) {
        super(application);
    }

    public boolean isDetailExist() {
        if (itemDetail == null) return false;
        return true;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItemDetail(ItemDetail itemDetail) {
        this.itemDetail = itemDetail;
    }

    public ItemDetail getItemDetail() {
        return itemDetail;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}