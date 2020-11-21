package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.dao.CartDAO;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.dao.ItemDAO;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.dao.ItemDetailDAO;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.ItemDetail;

@androidx.room.Database(entities = {Cart.class, Item.class, ItemDetail.class}, version = 1)
public abstract class Database extends RoomDatabase {
    private static Database instance;

    public abstract CartDAO cartDAO();

    public abstract ItemDAO itemDAO();

    public abstract ItemDetailDAO itemDetailDAO();

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new InitiateDbAsyncTask(instance).execute();
        }
    };

    private static class InitiateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private CartDAO asyncCartDAO;

        private InitiateDbAsyncTask(Database database) {
            this.asyncCartDAO = database.cartDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncCartDAO.insert(new Cart("Cart Obj#1", 16, 0));
            asyncCartDAO.insert(new Cart("Cart Obj#2", 15, 40));
            return null;
        }
    }
}