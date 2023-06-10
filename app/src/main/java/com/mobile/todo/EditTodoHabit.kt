package com.mobile.todo

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.database.dataset.Habit
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class EditTodoHabit : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)

        val dateButton = findViewById<Button>(R.id.date)
        val title = findViewById<EditText>(R.id.title)
        val description = findViewById<EditText>(R.id.description)

        if (intent.hasExtra(EXTRA_DATA)) {
            val data = intent.getSerializableExtra(EXTRA_DATA) as TYPE
            if (data == TYPE.HABIT) {
                dateButton.visibility = View.GONE
            }
        }

        dateButton.setOnClickListener {
            // Get current date
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // Date Picker Dialog
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in text view
                    dateButton.text = "$dayOfMonth/${monthOfYear + 1}/$year"
                },
                year,
                month,
                day
            )
            dpd.show() // Show DatePickerDialog
        }

        findViewById<Button>(R.id.confirm).setOnClickListener {
            if(title.text.toString() == ""){
                Toast.makeText(this, "Title must not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            GlobalScope.launch {
                AppDatabase.getDatabase(this@EditTodoHabit).habitDao().insertHabit(
                    Habit(
                        title.text.toString(),
                        description.text.toString(),
                        HomePage.USER_ID.toInt()
                    )
                )
                startActivity(Intent(this@EditTodoHabit, HomePage::class.java))
            }
        }

        findViewById<Button>(R.id.cancel).setOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        private const val EXTRA_DATA = "type"

        enum class TYPE {
            HABIT,
            TODO
        }

        fun newInstance(context: Context, data: TYPE): Intent {
            return Intent(context, EditTodoHabit::class.java).apply {
                putExtra(EXTRA_DATA, data)
            }
        }
    }
}
