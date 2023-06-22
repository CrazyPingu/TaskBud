package com.mobile.todo

import android.content.Intent
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
import com.mobile.todo.utils.Constant
import kotlin.properties.Delegates

class HomePage : AppCompatActivity() {

    companion object {
        var pageToShow: Int = R.id.navbar_todo
        var USER_ID by Delegates.notNull<Int>()
    }

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (Constant.getUser(this) != -1) {
            USER_ID = Constant.getUser(this)
        }

        if(intent.hasExtra("page")){
            pageToShow = intent.getIntExtra("page", R.id.navbar_todo)
        }

        Constant.setTheme(this)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            pageToShow = item.itemId
            when (item.itemId) {
                R.id.navbar_todo -> {
                    changeFragment(TodoPage.newInstance(USER_ID))
                    true
                }
                R.id.navbar_habit -> {
                    changeFragment(HabitPage.newInstance(USER_ID))
                    true
                }
                R.id.navbar_profile -> {
                    if (intent.hasExtra("profilePic")) {
                        val profilePic = intent.getParcelableExtra<Uri>("profilePic")
                        changeFragment(ProfilePage.newInstance(profilePic!!))
                    } else {
                        changeFragment(ProfilePage.newInstance())
                    }
                    true
                }
                R.id.navbar_settings -> {
                    changeFragment(
                        SettingsPage.newInstance(AppCompatDelegate.getDefaultNightMode())
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
        bottomNavigationView.selectedItemId = pageToShow
    }

    override fun onBackPressed() {
        startActivity(Intent(this, Login::class.java))
    }
}