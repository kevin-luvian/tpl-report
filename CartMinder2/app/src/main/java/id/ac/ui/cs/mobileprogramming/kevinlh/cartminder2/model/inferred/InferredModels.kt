package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.inferred

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Detail
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Item


data class ItemWithDetail(val item: Item = Item(), var detail: Detail? = null) {
    override fun equals(other: Any?): Boolean {
        if (other is ItemWithDetail) {
            return item == other.item && detail == other.detail
        }
        throw RuntimeException("can't compare to non ItemWithDetail object")
    }
}