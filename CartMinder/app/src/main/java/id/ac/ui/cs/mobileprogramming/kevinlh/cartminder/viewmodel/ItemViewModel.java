package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.ItemDetail;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.repository.ItemRepository;

public class ItemViewModel extends AndroidViewModel {
    private final ItemRepository itemRepository;
    private Item item;
    private ItemDetail itemDetail;
    private int position;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
    }

    public void initializeItemDetail() {
        ItemDetail itemDetail = itemRepository.getItemDetail(item);
        if (itemDetail == null) itemDetail = new ItemDetail();
        itemDetail.setItemId(item.getId());
        this.itemDetail = itemDetail;
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

    public void saveItem() {
        itemRepository.insert(item);
    }

    public void saveDetail() {
        long detailID = itemRepository.insertDetail(itemDetail);
        item.setDetailId(detailID);
        itemRepository.insert(item);
    }
}