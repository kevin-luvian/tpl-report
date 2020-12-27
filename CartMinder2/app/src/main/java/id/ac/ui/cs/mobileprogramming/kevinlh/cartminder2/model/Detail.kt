package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "detail_table")
class Detail() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    @ForeignKey(
        entity = Item::class,
        parentColumns = ["id"],
        childColumns = ["itemId"],
        onDelete = ForeignKey.CASCADE
    )
    var itemId: Long = 0L
    var imagePath: String? = null
    var description = ""
    var tags: MutableList<String> = mutableListOf()

    override fun equals(other: Any?): Boolean {
        if (other is Detail)
            return itemId == other.itemId &&
                    imagePath == other.imagePath &&
                    description == other.description &&
                    tags == other.tags
        throw RuntimeException("can't compare to non Detail object")
    }

    override fun toString() = StringBuilder().apply {
        append("Detail::\n")
        append("    id      : $id\n")
        append("    imageUri: $imagePath\n")
        append("    tags    : $tags\n")
    }.toString()

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        itemId = parcel.readLong()
        imagePath = parcel.readString()
        description = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(itemId)
        parcel.writeString(imagePath)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Detail> {
        override fun createFromParcel(parcel: Parcel): Detail {
            return Detail(parcel)
        }

        override fun newArray(size: Int): Array<Detail?> {
            return arrayOfNulls(size)
        }
    }
}