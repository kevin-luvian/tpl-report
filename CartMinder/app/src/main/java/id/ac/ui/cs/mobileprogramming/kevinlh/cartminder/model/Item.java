package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


@Entity(tableName = "item_table")
public class Item {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ForeignKey(
            entity = Cart.class,
            parentColumns = "id",
            childColumns = "cartId",
            onDelete = ForeignKey.CASCADE
    )
    private long cartId;
    @ForeignKey(
            entity = ItemDetail.class,
            parentColumns = "id",
            childColumns = "detailId",
            onDelete = ForeignKey.NO_ACTION
    )
    private long detailId;
    private String title;
    private String description;
    private int price;

    public Item() {
        this("", "", 0);
    }

    public Item(String title, String description, int price) {
        this.id = 0;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public long getDetailId() {
        return detailId;
    }

    public void setDetailId(long detailId) {
        this.detailId = detailId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPriceToString() {
        return String.format(Locale.US, "%,d IDR", price);
    }

    @NotNull
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", cartId=" + cartId +
                ", detailId=" + detailId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
