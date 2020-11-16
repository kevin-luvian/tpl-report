package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "itemDetail_table")
public class ItemDetail {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ForeignKey(
            entity = Item.class,
            parentColumns = "id",
            childColumns = "itemId",
            onDelete = CASCADE
    )
    private long itemId;
    private String imagePath;
    private String category;
    private String weight;
    private String detail;

    public ItemDetail() {
        this("", "", "", "");
    }

    public ItemDetail(String imagePath, String category, String weight, String detail) {
        this.id = 0;
        this.imagePath = imagePath;
        this.category = category;
        this.weight = weight;
        this.detail = detail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "ItemDetail{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", imagePath='" + imagePath + '\'' +
                ", category='" + category + '\'' +
                ", weight='" + weight + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
