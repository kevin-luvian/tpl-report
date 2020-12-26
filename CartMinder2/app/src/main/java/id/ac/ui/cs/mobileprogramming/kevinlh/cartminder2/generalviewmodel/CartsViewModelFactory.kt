package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.generalviewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CartsViewModelFactory(private val application: Application):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartsViewModel::class.java)) {
            return CartsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}