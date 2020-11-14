package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table")
public class Cart {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String time;

    public Cart() {
        this("", "");
    }

    public Cart(String title, String time) {
        this.title = title;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
