package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.dao

import androidx.room.*
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Item
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemDao {
    @Insert
    suspend fun insert(item: Item): Long

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM item_table WHERE cartId=:cartId")
    suspend fun findByCartId(cartId: Long): List<Item>

    @Query("SELECT * FROM item_table")
    fun findAll(): Flow<List<Item>>
}