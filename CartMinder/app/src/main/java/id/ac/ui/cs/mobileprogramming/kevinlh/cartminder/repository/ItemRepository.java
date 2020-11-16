package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.lang.ref.WeakReference;
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

    public List<Item> getCartItems(Cart cart) {
        try {
            GetCartItemsAsyncTask task = new GetCartItemsAsyncTask();
            task.setWeakReference(new WeakReference<ItemRepository>(this));
            return task.execute(cart).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("[error]" + e.getMessage());
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

    public void insertAll(List<Item> items, long cartId) {
        daoExecutor.execute(() -> {
            for (Item item : items) {
                item.setCartId(cartId);
                itemDAO.insert(item);
            }
        });
    }

    public void deleteAll(List<Item> items) {
        daoExecutor.execute(() -> {
            for (Item item : items) {
                itemDAO.delete(item);
            }
        });
    }

    private static class GetCartItemsAsyncTask extends AsyncTask<Cart, Void, List<Item>> {
        private WeakReference<ItemRepository> weakReference;

        public void setWeakReference(WeakReference<ItemRepository> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        protected List<Item> doInBackground(Cart... carts) {
            ItemRepository reference = weakReference.get();
            if (reference == null) return null;
            return reference.itemDAO.getCartItems(carts[0].getId());
        }
    }
}
