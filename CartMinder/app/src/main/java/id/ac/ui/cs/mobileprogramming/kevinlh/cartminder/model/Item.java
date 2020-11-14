package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "item_table")
public class Item implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ForeignKey(
            entity = Cart.class,
            parentColumns = "id",
            childColumns = "cartId",
            onDelete = CASCADE
    )
    private long cartId;
    private String title;
    private String description;
    private int price;

    public Item() {
        this("", "", 0);
    }

    public Item(String title, String description, int price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    protected Item(Parcel in) {
        id = in.readLong();
        cartId = in.readLong();
        title = in.readString();
        description = in.readString();
        price = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

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

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", cartId=" + cartId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(cartId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(price);
    }
}
