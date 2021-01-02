package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.glscreen

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class OpenGLActivity : AppCompatActivity() {

    private lateinit var glSurface: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurface = GLSurfaceView(this)
        glSurface.setRenderer(GLRenderer())
        setContentView(glSurface)

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

    override fun onResume() {
        super.onResume()
        glSurface.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurface.onPause()
    }
}