package com.mobile.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navbar_todo -> {
                    val fragment = TodoPage()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit()
                    true
                }
                R.id.navbar_habit -> {
                    // Handle item 2 selection
                    true
                }
                R.id.navbar_profile -> {
                    // Handle item 3 selection
                    true
                }
                R.id.navbar_settings -> {
                    // Handle item 3 selection
                    true
                }
                else -> false
            }
        }


    }
}