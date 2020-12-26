package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2

import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.dao.CartDao
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.CartMinderDatabase
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4ClassRunner::class)
class DatabaseTest {
    private lateinit var cartDao: CartDao
    private lateinit var db: CartMinderDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, CartMinderDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        cartDao = db.cartDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val cart = Cart("Test")
        cartDao.insert(cart)
        val carts = cartDao.getCarts().value
        assertTrue(carts?.contains(cart) ?: false)
    }
}