package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model;

import android.annotation.SuppressLint;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "cart_table")
public class Cart {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private int hour;
    private int minute;

    public Cart() {
        Calendar cal = Calendar.getInstance();
        this.id = 0;
        this.title = "";
        this.hour = cal.get(Calendar.HOUR_OF_DAY);
        this.minute = cal.get(Calendar.MINUTE);
    }

    public Cart(String title, int hour, int minute) {
        this.id = 0;
        this.title = title;
        this.hour = hour;
        this.minute = minute;
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

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @SuppressLint("DefaultLocale")
    public String getTimeString() {
        return String.format("%02d:%02d", hour, minute);
    }

    public Calendar getTimeAsCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        return cal;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", time=" + getTimeString() +
                '}';
    }
}
