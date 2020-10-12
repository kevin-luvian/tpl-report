package com.example.app4_fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app4_fragment.fragments.ContentFragment
import com.example.app4_fragment.fragments.MainFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainFragment = MainFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainlayout, mainFragment)
            commit()
        }
    }
}