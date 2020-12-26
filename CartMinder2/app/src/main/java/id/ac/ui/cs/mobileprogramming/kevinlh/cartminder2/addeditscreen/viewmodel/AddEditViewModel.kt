package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.CartRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.DetailRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.ItemRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Detail
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Item
import kotlinx.coroutines.launch

class AddEditViewModel(application: Application) : ViewModel() {
    private val cartRepo = CartRepository(application)
    private val itemRepo = ItemRepository(application)
    private val detailRepo = DetailRepository(application)
    var cart: Cart? = null
        set(value) {
            field = value ?: Cart()
            findCartLinks()
        }
    val itemWithDetails: MutableLiveData<MutableList<ItemWithDetail>> = MutableLiveData(
        mutableListOf()
    )
    private var deletedItem: MutableList<Item> = mutableListOf()
    private var deletedDetail: MutableList<Detail> = mutableListOf()
    val liveTagsHolder: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    private fun findCartLinks() {
        cart?.apply {
            if (id > 0) viewModelScope.launch {
                val items = itemRepo.findByCartId(id)
                val itw = items.map { item ->
                    ItemWithDetail(item, detailRepo.findByItemId(item.id))
                }
                itemWithDetails.value = itw.toMutableList()
            }
        }
    }

    fun getIWD(pos: Int): ItemWithDetail? {
        itemWithDetails.value?.let { list ->
            if (pos < list.size) return list[pos]
        }
        return null
    }

    fun removeItem(pos: Int) {
        itemWithDetails.value?.apply {
            if (size > pos) {
                removeAt(pos).item.let {
                    if (it.id > 0L) deletedItem.add(it)
                }
            }
            itemWithDetails.value = toMutableList()
        }
    }

    fun createNewItem(): Int {
        var listSize = -1
        itemWithDetails.value?.apply {
            add(ItemWithDetail())
            listSize = size - 1
        }
        return listSize
    }

    fun removeDetail(pos: Int) {
        itemWithDetails.value?.let { mlist ->
            if (mlist.size > pos) {
                mlist[pos].detail?.let {
                    if (it.id > 0L) deletedDetail.add(it)
                    mlist[pos].detail = null
                }
            }
        }
    }

    fun createNewDetail(pos: Int) {
        itemWithDetails.value?.get(pos)!!.apply {
            detail = Detail()
        }
    }

    fun clearData() {
        cart = null
        itemWithDetails.value = mutableListOf()
        deletedItem = mutableListOf()
    }

    fun saveData() {
        viewModelScope.launch {
            cart?.let {
                val cartId = saveCart(it)
                saveItemWithDetailList(cartId)
                deleteDeletedList()
            }
        }
    }

    private fun deleteDeletedList() {
        deletedItem.forEach {
            itemRepo.deleteItem(it)
        }
        deletedDetail.forEach {
            detailRepo.deleteDetail(it)
        }
    }

    private fun saveItemWithDetailList(cartId: Long) {
        viewModelScope.launch {
            itemWithDetails.value?.forEach { iwd ->
                iwd.item.cartId = cartId
                saveItemWithDetail(iwd)
            }
        }
    }

    private fun saveItemWithDetail(iwd: ItemWithDetail) {
        viewModelScope.launch {
            iwd.apply {
                val itemId = saveItem(item)
                detail?.let {
                    it.itemId = itemId
                    saveDetail(it)
                }
            }
        }
    }

    private suspend fun saveCart(c: Cart): Long {
        val cartId: Long
        if (c.id > 0) {
            cartId = c.id
            cartRepo.updateCart(c)
        } else {
            cartId = cartRepo.insertCart(c)
        }
        return cartId
    }

    private suspend fun saveItem(i: Item): Long {
        val itemId: Long
        if (i.id > 0) {
            itemId = i.id
            itemRepo.updateItem(i)
        } else {
            itemId = itemRepo.insertItem(i)
        }
        return itemId
    }

    private fun saveDetail(d: Detail) {
        viewModelScope.launch {
            if (d.id > 0) detailRepo.updateDetail(d)
            else detailRepo.insertDetail(d)
        }
    }

    fun setLiveTagHolder(mList: MutableList<String>) {
        liveTagsHolder.value = mList
    }

    fun removeLiveTagHolder(pos: Int) {
        liveTagsHolder.value?.apply {
            removeAt(pos)
            setLiveTagHolder(toMutableList())
        }
    }

//    fun deleteFromLiveTagHolder(pos:Int){
//        liveTagsHolder.value?.apply {
//            val mList = toMutableList()
//            mList.removeAt(pos)
//            toList()
//        }
//    }
}

data class ItemWithDetail(val item: Item = Item(), var detail: Detail? = null)