package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.dao;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;

@Dao
public interface ItemDAO {
    @Insert
    void insert(Item item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @WorkerThread
    void insertAll(List<Item> items);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

    @Query("SELECT * FROM item_table")
    LiveData<List<Item>> getItems();

    @Query("SELECT * FROM item_table WHERE cartId=:cartId")
    List<Item> getCartItems(Long cartId);
}
