package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.ItemDetail;

@Dao
public interface ItemDetailDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ItemDetail itemDetail);

    @Delete
    void delete(ItemDetail itemDetail);

    @Query("SELECT * FROM itemDetail_table WHERE id=:id")
    ItemDetail findById(long id);
}