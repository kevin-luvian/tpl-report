package id.ac.ui.cs.mobileprogramming.kevinlh.app7_native

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    var count = 0
    lateinit var tvCount: TextView
    lateinit var btnCount: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvCount = findViewById(R.id.text_count)
        btnCount = findViewById(R.id.count_btn)
    }

    override fun onStart() {
        super.onStart()
        btnCount.setOnClickListener {
            ++count
            updateTvCount()
        }
        updateTvCount()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("last_count",count)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        count = savedInstanceState.getInt("last_count")
        updateTvCount()
    }

    fun updateTvCount() {
        tvCount.text = count.toString()
    }
}