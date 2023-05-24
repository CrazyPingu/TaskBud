package com.mobile.todo

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobile.todo.fragment.HabitPage
import com.mobile.todo.fragment.ProfilePage
import com.mobile.todo.fragment.SettingsPage
import com.mobile.todo.fragment.TodoPage

class HomePage : AppCompatActivity() {


    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        val userId = intent.getIntExtra("userId", 0)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navbar_todo -> {
                    changeFragment(TodoPage.newInstance(userId))
                    true
                }
                R.id.navbar_habit -> {
                    changeFragment(HabitPage.newInstance(userId))
                    true
                }
                R.id.navbar_profile -> {
                    changeFragment(ProfilePage.newInstance(userId))
                    true
                }
                R.id.navbar_settings -> {
                    changeFragment(
                        SettingsPage.newInstance(
                            this@HomePage,
                            AppCompatDelegate.getDefaultNightMode()
                        )
                    )
                    true
                }
                else -> false
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        // Select the first item by default
        bottomNavigationView.selectedItemId = R.id.navbar_todo
    }

}