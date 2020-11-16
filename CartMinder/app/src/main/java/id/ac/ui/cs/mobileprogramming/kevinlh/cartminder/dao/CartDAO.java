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
    long insert(Cart cart);

    @Update
    void update(Cart cart);

    @Delete
    void delete(Cart cart);

    @Query("SELECT * FROM cart_table ORDER BY hour ASC, minute ASC")
    LiveData<List<Cart>> getCartsOrdered();

    @Query("SELECT * FROM cart_table")
    LiveData<List<Cart>> getCarts();
}