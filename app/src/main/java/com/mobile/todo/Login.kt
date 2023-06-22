package com.mobile.todo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.utils.Constant
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

        Constant.setTheme(this)
        rememberMe = findViewById(R.id.remember_me)
        if(Constant.getUser(this) != -1){
            loginUser(Constant.getUser(this))
        }

        Shortcut.removeTodoHabit(this)
        Shortcut.addSignup(this)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        database = AppDatabase.getDatabase(this)

        // Remove for production ////////////
        username.text.append("a")
        password.text.append("a")
        /////////////////////////////////////

        // Redirect to Signup Activity
        findViewById<Button>(R.id.signup_button).setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
        }

        findViewById<Button>(R.id.login_button).setOnClickListener {
            checkLoginUser()
        }
    }

    private fun loginUser(userId : Int) {
        if(rememberMe.isChecked){
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