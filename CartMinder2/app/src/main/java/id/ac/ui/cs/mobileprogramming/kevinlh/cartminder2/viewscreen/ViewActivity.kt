package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import kotlinx.android.synthetic.main.activity_view.*


class ViewActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        toggle = ActionBarDrawerToggle(this, main_drawer, R.string.open, R.string.close)
        main_drawer.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_sync -> {
                    Toast.makeText(applicationContext, "sync menu clicked", Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.menu_opengl -> {
                    Toast.makeText(applicationContext, "opengl menu clicked", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}