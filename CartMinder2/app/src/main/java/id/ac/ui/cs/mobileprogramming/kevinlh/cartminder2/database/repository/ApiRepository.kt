package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository

import android.app.Application
import android.util.Log
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.CartMinderDatabase
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.api.ApiClient
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.api.model.CartApiModel
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.api.model.CartApiModelPost
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.dao.CartDao
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.dao.DetailDao
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.dao.ItemDao
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.enums.MarketCategory
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Detail
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.inferred.ItemWithDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApiRepository(application: Application) {
    private val apiQuery = ApiClient.getCartMinderApi()
    val cartDao: CartDao
    val itemDao: ItemDao
    val detailDao: DetailDao

    init {
        val db = CartMinderDatabase.getInstance(application)
        cartDao = db.cartDao
        itemDao = db.itemDao
        detailDao = db.detailDao
    }

    suspend fun getCarts(): List<CartApiModel?> = apiQuery.getCartApis()

    suspend fun getCartApisToString(): String {
        val res = StringBuilder()
        getCarts().forEach {
            res.append(it ?: "None\n")
            res.append("\n")
        }
        return res.toString()
    }

    fun postTestCart() {
        val cp = CartApiModelPost().apply {
//            title = "Test Cart #${Utils.getRandomInt(0, 100)}"
            cart = Cart().apply {
                title = "This cart"
                category = MarketCategory.SUPERMARKET
            }
            iwdList = listOf(ItemWithDetail().apply {
                detail = Detail()
            })
        }
        CoroutineScope(Dispatchers.Default).launch { apiQuery.postCartApis(cp) }
    }

    fun postAllObjects(lmbd: () -> Unit = {}) {
        CoroutineScope(Dispatchers.IO).launch {
            val localObjects = convertLocalObjectsToModelPosts()
            val cloudObjects = apiQuery.getCartApisAsModelPost()
            val allObjects: Set<CartApiModelPost?> = cloudObjects.union(localObjects)
            postCartApis(allObjects.toList())
            withContext(Dispatchers.Main) { lmbd() }
        }
    }

    suspend fun postCartApis(mList: List<CartApiModelPost?>) {
        mList.forEach { model ->
            model?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    apiQuery.postCartApis(it)
                }
            }
        }
    }

    fun receiveCloudObjects(lmbd: () -> Unit = {}) {
        CoroutineScope(Dispatchers.IO).launch {
            val localObjects = convertLocalObjectsToModelPosts()
            val cloudObjects = apiQuery.getCartApisAsModelPost()
            val nonLocalObjects: List<CartApiModelPost?> =
                cloudObjects.filter { !localObjects.contains(it) }
            saveCartApisToLocal(nonLocalObjects)
            withContext(Dispatchers.Main) { lmbd() }
        }
    }

    fun deleteCloudObjects(lmbd: () -> Unit = {}) {
        CoroutineScope(Dispatchers.IO).launch {
            apiQuery.deleteAllCartApi()
            withContext(Dispatchers.Main) { lmbd() }
        }
    }

    fun deleteCloudObjectsManual(lmbd: () -> Unit = {}) {
        CoroutineScope(Dispatchers.IO).launch {
            val cloudObjects = apiQuery.getCartApis()
            cloudObjects.forEach { obj ->
                obj?.let { cApi ->
                    CoroutineScope(Dispatchers.IO).launch {
                        // Log.d("Delete", "deleting ${cApi.id}")
                        apiQuery.deleteCartApi(cApi.id)
                    }
                }
            }
            withContext(Dispatchers.Main) { lmbd() }
        }
    }

    fun syncObjects() {
        CoroutineScope(Dispatchers.Default).launch {
            val localObjects = convertLocalObjectsToModelPosts()
            val cloudObjects = apiQuery.getCartApisAsModelPost()
            val allObjects: Set<CartApiModelPost?> = cloudObjects.union(localObjects)
            val nonLocalObjects: List<CartApiModelPost?> =
                cloudObjects.filter { !localObjects.contains(it) }
            postCartApis(allObjects.toList())
            saveCartApisToLocal(nonLocalObjects)
        }
    }

    fun saveCartApisToLocal(mList: List<CartApiModelPost?>) {
        Log.d("Sync", "saving carts: $mList")
        mList.forEach { m ->
            m?.let { cApi ->
                CoroutineScope(Dispatchers.IO).launch {
                    cApi.cart?.let { cartDao.insert(it) }
                    cApi.iwdList.forEach { iwd ->
                        iwd?.run {
                            itemDao.insert(item)
                            detail?.let { detailDao.insert(it) }
                        }
                    }
                }
            }
        }
    }

    suspend fun convertCartApiModelToPosts(mList: List<CartApiModel?>):
            List<CartApiModelPost?> =
        mList.map { m -> m?.let { CartApiModelPost(it.cart, it.iwdList) } }

    private suspend fun convertLocalObjectsToModelPosts(): List<CartApiModelPost> {
        val localObjects = mutableListOf<CartApiModelPost>()
        cartDao.findAll().forEach { cart ->
            val iwds = mutableListOf<ItemWithDetail>()
            itemDao.findByCartId(cart.id).forEach { item ->
                val detail = detailDao.findByItemId(item.id)
                iwds.add(ItemWithDetail(item, detail))
            }
            localObjects.add(CartApiModelPost(cart, iwds))
        }
        return localObjects
    }
}