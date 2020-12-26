package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R

class AddEditActivity : AppCompatActivity() {
//    private fun Fragment.setActivityTitle(title: String) {
//        (activity as AppCompatActivity?)!!.supportActionBar?.title = title
//    }
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}