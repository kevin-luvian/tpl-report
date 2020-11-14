package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.dao.ItemDAO;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.database.Database;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;

public class ItemRepository {
    private ExecutorService daoExecutor;
    private ItemDAO itemDAO;

    public ItemRepository(Application application) {
        Database database = Database.getInstance(application);
        daoExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        itemDAO = database.itemDAO();
    }

    public List<Item> getCartItems(Cart cart){
        try {
            return daoExecutor.submit(() -> itemDAO.getCartItems(cart.getId())).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insert(Item item) {
        daoExecutor.execute(() -> {
            itemDAO.insert(item);
        });
    }

    public void update(Item item) {
        daoExecutor.execute(() -> {
            itemDAO.update(item);
        });
    }

    public void delete(Item item) {
        daoExecutor.execute(() -> {
            itemDAO.delete(item);
        });
    }

    public void insertAll(List<Item> items) {
        daoExecutor.execute(() -> {
            itemDAO.insertAll(items);
        });
    }
}
