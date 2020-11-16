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

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.dao.CartDAO;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.database.Database;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Item;

public class CartRepository {
    private ExecutorService daoExecutor;
    private CartDAO cartDAO;
    private LiveData<List<Cart>> carts;

    public CartRepository(Application application) {
        Database database = Database.getInstance(application);
        daoExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        cartDAO = database.cartDAO();
        carts = cartDAO.getCartsOrdered();
    }

    public long insert(Cart cart) {
        try {
            InsertCartAsyncTask task = new InsertCartAsyncTask(this);
            return task.execute(cart).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("[error]" + e.getMessage());
            return -1;
        }
    }

    public void update(Cart cart) {
        daoExecutor.execute(() -> {
            cartDAO.update(cart);
        });
    }

    public void delete(Cart cart) {
        daoExecutor.execute(() -> {
            cartDAO.delete(cart);
        });
    }

    public LiveData<List<Cart>> getCarts() {
        return carts;
    }

    private static class InsertCartAsyncTask extends AsyncTask<Cart, Void, Long> {
        private WeakReference<CartRepository> weakReference;

        InsertCartAsyncTask(CartRepository cartRepository) {
            weakReference = new WeakReference<CartRepository>(cartRepository);
        }

        @Override
        protected Long doInBackground(Cart... carts) {
            CartRepository reference = weakReference.get();
            if (reference == null) return null;
            return reference.cartDAO.insert(carts[0]);
        }
    }
}
