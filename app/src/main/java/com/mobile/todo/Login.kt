package com.mobile.todo

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.utils.Constant
import com.mobile.todo.utils.Monet
import com.mobile.todo.utils.Shortcut
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Login : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var database: AppDatabase
    private lateinit var rememberMe: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Shortcut.removeTodoHabit(this)
        Shortcut.addSignup(this)

        Constant.setTheme(this)

        rememberMe = findViewById(R.id.remember_me)

        val signupButton : Button = findViewById(R.id.signup_button)
        val loginButton : Button = findViewById(R.id.login_button)


        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        database = AppDatabase.getDatabase(this)

        // Set monet color
        if (Constant.getMonet(this)) {

            Monet.setTextInputLayoutMonet(findViewById(R.id.username_layout), this)
            Monet.setTextInputLayoutMonet(findViewById(R.id.password_layout), this, true)

            findViewById<FrameLayout>(R.id.card).background = Monet.setBorderColorMonet(this)

            Monet.setCheckBoxMonet(rememberMe, this)

            Monet.setStatusBarMonet(this, window)

            Monet.setButtonMonet(signupButton, this)
            Monet.setButtonMonet(loginButton, this)
        }

        Constant.refreshWidget(this)

        if (Constant.getUser(this) != -1) {
            rememberMe.isChecked = true
            loginUser(Constant.getUser(this))
        }

        // Redirect to Signup Activity
        signupButton.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
        }

        loginButton.setOnClickListener {
            checkLoginUser()
        }
    }

    private fun loginUser(userId: Int) {
        if (rememberMe.isChecked) {
            Constant.saveUser(this@Login, userId)
            Shortcut.removeSignup(this)
            Shortcut.addTodoHabit(this)
        }

        HomePage.pageToShow = R.id.navbar_todo
        HomePage.USER_ID = userId
        startActivity(Intent(this, HomePage::class.java))
    }

    private fun checkLoginUser() {
        val enteredUsername = username.text.toString().trim()
        val enteredPassword = password.text.toString().trim()

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            Toast.makeText(this, "Username and Password must not be empty", Toast.LENGTH_SHORT)
                .show()
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            val user = withContext(Dispatchers.IO) {
                database.userDao().getUser(enteredUsername, enteredPassword)
            }
            runOnUiThread {
                if (user != null) {
                    loginUser(user.id)
                } else {
                    Toast.makeText(
                        this@Login,
                        "Username or Password is incorrect",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}