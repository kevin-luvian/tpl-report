package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "detail_table")
class Detail {
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

    override fun toString(): String {
        val s = StringBuilder()
        s.append("id: $id\n")
        s.append("imageUri: $imagePath\n")
        s.append("tags: $tags")
        return s.toString()
    }
}