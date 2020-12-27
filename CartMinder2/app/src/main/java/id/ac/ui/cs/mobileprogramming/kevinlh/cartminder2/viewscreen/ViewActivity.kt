package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.ApiRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.CartRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.helper.NetworkHelper
import kotlinx.android.synthetic.main.activity_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewActivity : AppCompatActivity() {
    private lateinit var networkHelper: NetworkHelper
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var apiRepo: ApiRepository
    private lateinit var cartRepo: CartRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        networkHelper = NetworkHelper.getInstance(applicationContext)
        apiRepo = ApiRepository(application)
        cartRepo = CartRepository(application)
        toggle = ActionBarDrawerToggle(this, main_drawer, R.string.open, R.string.close)
        main_drawer.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nav_view.setNavigationItemSelectedListener(navigationListener())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigationListener() =
        NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_delete_local -> {
                    cartRepo.deleteAll()
                    Toast.makeText(applicationContext, "local objects deleted", Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.menu_post_objs -> networkHelper.checkConnection {
                    apiRepo.postAllObjects {
                        Toast.makeText(
                            applicationContext, "all objects posted", Toast.LENGTH_SHORT
                        ).show()
                    }
                    // apiRepo.postTestCart()
                }
                R.id.menu_delete_objs -> networkHelper.checkConnection {
                    apiRepo.deleteCloudObjects {
                        Toast.makeText(
                            applicationContext, "all objects deleted", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                R.id.menu_receive_objs -> networkHelper.checkConnection {
                    apiRepo.receiveCloudObjects {
                        Toast.makeText(
                            applicationContext, "objects received", Toast.LENGTH_SHORT
                        ).show()
                    }
                    // goToSandboxFragment()
                }
                R.id.menu_sync -> networkHelper.checkConnection {
                    //                    apiRepo.syncObjects()
                    Toast.makeText(
                        applicationContext, "sync menu clicked", Toast.LENGTH_SHORT
                    ).show()
                }
                R.id.menu_opengl ->
                    Toast.makeText(applicationContext, "opengl menu clicked", Toast.LENGTH_SHORT)
                        .show()
            }
            true
        }

    private fun goToSandboxFragment() {
        CoroutineScope(Dispatchers.Default).launch {
            val textBody = apiRepo.getCartApisToString()
            withContext(Dispatchers.Main) {
                val fragment = SandBoxFragment.getInstance(textBody)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment, "SandBox")
                    .commit()
            }
        }
    }
}