package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.enums.MarketCategory
import java.util.*
import java.util.Locale.ROOT


@Entity(tableName = "cart_table")
class Cart(title: String = "") : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var title: String = title.capitalize(ROOT)
    var category: MarketCategory = MarketCategory.UNSPECIFIED
    var active: Boolean = false
    var calendar: Calendar = Calendar.getInstance()

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        title = parcel.readString().toString()
        active = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeByte(if (active) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cart> {
        override fun createFromParcel(parcel: Parcel): Cart {
            return Cart(parcel)
        }

        override fun newArray(size: Int): Array<Cart?> {
            return arrayOfNulls(size)
        }
    }
}