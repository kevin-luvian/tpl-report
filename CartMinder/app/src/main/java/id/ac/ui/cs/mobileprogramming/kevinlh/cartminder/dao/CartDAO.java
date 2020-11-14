package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;

@Dao
public interface CartDAO {
    @Insert
    Long insert(Cart cart);

    @Update
    void update(Cart cart);

    @Delete
    void delete(Cart cart);

    @Query("SELECT * FROM cart_table ORDER BY time ASC")
    LiveData<List<Cart>> getCarts();
}