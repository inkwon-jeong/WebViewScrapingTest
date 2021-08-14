package com.example.webviewscrapingtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.webviewscrapingtest.ui.result.ResultFragment
import com.example.webviewscrapingtest.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SearchFragment.newInstance())
                .commitNow()
        }
    }

    fun navigate(query: String) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container, ResultFragment.newInstance(query))
            .commit()
    }
}