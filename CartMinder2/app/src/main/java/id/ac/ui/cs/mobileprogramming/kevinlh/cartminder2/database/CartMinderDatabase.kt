package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database;

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.dao.CartDao
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.dao.DetailDao
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.dao.ItemDao
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Detail
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Cart::class, Item::class, Detail::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CartMinderDatabase : RoomDatabase() {
    abstract val cartDao: CartDao
    abstract val itemDao: ItemDao
    abstract val detailDao: DetailDao

    companion object {

        @Volatile
        private var instance: CartMinderDatabase? = null

        fun getInstance(context: Context): CartMinderDatabase {
            synchronized(this) {
                var dbInstance = instance
                if (instance == null) {
                    dbInstance = Room.databaseBuilder(
                        context.applicationContext,
                        CartMinderDatabase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                    instance = dbInstance
                }
                return dbInstance!!
            }
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    for (i in 1..20) {
                        val cartTitle = "cart #${i}"
                        instance!!.cartDao.insert(Cart(cartTitle))
                    }
                }
            }
        }
    }
}
