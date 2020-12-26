package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart

class AddEditViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditViewModel::class.java)) {
            return AddEditViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}