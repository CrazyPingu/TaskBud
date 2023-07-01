package com.mobile.todo

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobile.todo.fragment.HabitPage
import com.mobile.todo.fragment.ProfilePage
import com.mobile.todo.fragment.SettingsPage
import com.mobile.todo.fragment.TodoPage
import com.mobile.todo.utils.Constant
import com.mobile.todo.utils.Monet
import kotlin.properties.Delegates


class HomePage : AppCompatActivity() {

    companion object {
        var pageToShow: Int = R.id.navbar_todo
        var USER_ID by Delegates.notNull<Int>()
    }

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var todoPage: TodoPage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (Constant.getUser(this) != -1) {
            USER_ID = Constant.getUser(this)
        }

        if (intent.hasExtra("page")) {
            pageToShow = intent.getIntExtra("page", R.id.navbar_todo)
        }

        Constant.setTheme(this)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        if (Constant.getMonet(this)) {
            val colorAccent1 = resources.getColor(android.R.color.system_accent1_300, theme)
            bottomNavigationView.itemActiveIndicatorColor = ColorStateList.valueOf(colorAccent1)

            Monet.setStatusBarMonet(this, window)
        }


        bottomNavigationView.setOnItemSelectedListener { item ->
            pageToShow = item.itemId
            when (item.itemId) {
                R.id.navbar_todo -> {
                    todoPage = TodoPage.newInstance(USER_ID)
                    changeFragment(todoPage)
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

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when(pageToShow) {
                    R.id.navbar_profile, R.id.navbar_habit, R.id.navbar_settings -> {
                        pageToShow = R.id.navbar_todo
                        startActivity(Intent(this@HomePage, HomePage::class.java))
                        return
                    }
                    R.id.navbar_todo -> {
                        pageToShow = R.id.navbar_todo
                        startActivity(Intent(this@HomePage, HomePage::class.java))
                        return
                    }
                }
            }
        })
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

}