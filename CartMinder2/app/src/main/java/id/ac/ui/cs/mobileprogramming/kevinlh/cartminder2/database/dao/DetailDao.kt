package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.dao

import androidx.room.*
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Detail


@Dao
interface DetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(detail: Detail)

    @Update
    suspend fun update(detail: Detail)

    @Delete
    suspend fun delete(detail: Detail)

    @Query("SELECT * FROM detail_table WHERE itemId=:itemId")
    suspend fun findByItemId(itemId: Long): Detail?
}