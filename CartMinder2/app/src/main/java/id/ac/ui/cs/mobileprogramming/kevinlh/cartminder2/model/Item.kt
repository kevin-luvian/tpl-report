package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
class Item(var title: String = "", var price: Int = 0) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ForeignKey(
        entity = Cart::class,
        parentColumns = ["id"],
        childColumns = ["cartId"],
        onDelete = ForeignKey.CASCADE
    )
    var cartId: Long = 0
}