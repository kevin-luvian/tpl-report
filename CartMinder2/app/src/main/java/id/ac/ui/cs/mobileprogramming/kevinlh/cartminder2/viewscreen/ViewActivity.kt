package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.viewscreen

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.ApiRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.CartRepository
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen.OpenGLActivity
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.helper.NetworkHelper
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.service.SyncService
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

        // Receiveing cloud objects
        SyncService.scheduleJob(applicationContext)

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
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    private fun navigationListener() =
        NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_delete_local -> {
                    cartRepo.deleteAll()
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.delete_local),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                R.id.menu_post_objs -> networkHelper.checkConnection {
                    apiRepo.postAllObjects {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.api_post),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    // apiRepo.postTestCart()
                }
                R.id.menu_delete_objs -> networkHelper.checkConnection {
                    apiRepo.deleteCloudObjects {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.api_delete),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                R.id.menu_receive_objs -> networkHelper.checkConnection {
                    apiRepo.receiveCloudObjects {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.api_get),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    // goToSandboxFragment()
                }
                R.id.menu_sync -> networkHelper.checkConnection {
                    //                    apiRepo.syncObjects()
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.synchronize),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                R.id.menu_opengl -> {
                    val intent = Intent(applicationContext, OpenGLActivity::class.java)
                    startActivity(intent)
                }
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