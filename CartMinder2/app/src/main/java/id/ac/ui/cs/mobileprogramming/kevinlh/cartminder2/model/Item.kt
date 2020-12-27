package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
class Item() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ForeignKey(
        entity = Cart::class,
        parentColumns = ["id"],
        childColumns = ["cartId"],
        onDelete = ForeignKey.CASCADE
    )
    var cartId: Long = 0
    var title: String = ""
    var price: Int = 0

    override fun equals(other: Any?): Boolean {
        if (other is Item)
            return cartId == other.cartId &&
                    title == other.title &&
                    price == other.price
        throw RuntimeException("can't compare to non Item object")
    }

    @SuppressLint("DefaultLocale")
    override fun toString(): String {
        val s = StringBuilder().apply {
            append("Item::\n")
            append("  title: $title\n")
            append("  cartId: $cartId\n")
            append("  price: $price\n")
        }
        return s.toString()
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        cartId = parcel.readLong()
        title = parcel.readString() ?: ""
        price = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(cartId)
        parcel.writeString(title)
        parcel.writeInt(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}