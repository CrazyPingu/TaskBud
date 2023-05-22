package com.mobile.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobile.todo.fragment.HabitPage
import com.mobile.todo.fragment.TodoPage

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // TODO - Get the user ID from the login page
        val userId = 1

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
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

        // Preset the navbar "To Do's" page
        bottomNavigationView.selectedItemId = R.id.navbar_todo
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}