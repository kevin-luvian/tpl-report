package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.dao

import androidx.room.*
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import kotlinx.coroutines.flow.Flow


@Dao
interface CartDao {
    @Insert
    suspend fun insert(cart: Cart): Long

    @Update
    suspend fun update(cart: Cart)

    @Delete
    suspend fun delete(cart: Cart)

    @Query("SELECT * FROM cart_table WHERE id=:cartId")
    suspend fun findById(cartId: Long): Cart

    @Query("SELECT * FROM cart_table")
    fun getCarts(): Flow<List<Cart>>
}