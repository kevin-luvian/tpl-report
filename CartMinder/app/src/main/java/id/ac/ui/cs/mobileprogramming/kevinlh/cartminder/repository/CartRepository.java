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

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.dao.CartDAO;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.database.Database;
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.model.Cart;

public class CartRepository {
    private ExecutorService daoExecutor;
    private CartDAO cartDAO;
    private LiveData<List<Cart>> carts;

    public CartRepository(Application application) {
        Database database = Database.getInstance(application);
        daoExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        cartDAO = database.cartDAO();
        carts = cartDAO.getCarts();
    }

    public Long insert(Cart cart) {
        try {
            return daoExecutor.submit(() -> cartDAO.insert(cart)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
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
}
