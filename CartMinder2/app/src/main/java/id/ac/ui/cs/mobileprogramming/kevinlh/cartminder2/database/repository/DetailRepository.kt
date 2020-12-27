package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository

import android.app.Application
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.CartMinderDatabase
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.dao.DetailDao
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Detail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailRepository(application: Application) {
    private val detailDao: DetailDao

    init {
        val database = CartMinderDatabase.getInstance(application)
        detailDao = database.detailDao
    }

    suspend fun findByItemId(id: Long): Detail? = detailDao.findByItemId(id)

    suspend fun insertDetail(detail: Detail) = detailDao.insert(detail)

    fun updateDetail(detail: Detail) =
        CoroutineScope(Dispatchers.IO).launch { detailDao.update(detail) }

    fun deleteDetail(detail: Detail) =
        CoroutineScope(Dispatchers.IO).launch { detailDao.delete(detail) }

    companion object {
        @Volatile
        private var instance: DetailRepository? = null
        fun getInstance(application: Application): DetailRepository =
            instance ?: DetailRepository(application).also { instance = it }
    }
}