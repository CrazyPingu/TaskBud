package com.mobile.todo

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.database.dataset.Habit
import com.mobile.todo.database.dataset.Tag
import com.mobile.todo.database.dataset.ToDo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class EditTodoHabit : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)

        val dateButton = findViewById<Button>(R.id.date)
        val title = findViewById<EditText>(R.id.title)
        val description = findViewById<EditText>(R.id.description)
        val tag = findViewById<EditText>(R.id.tag)

        val type = intent.getSerializableExtra(TYPE_EXTRA) as TYPE
        if (type == TYPE.HABIT) {
            dateButton.visibility = View.GONE
            findViewById<EditText>(R.id.tag).visibility = View.GONE
            findViewById<ImageView>(R.id.favourite).visibility = View.GONE
        }

        if(intent.hasExtra(ID_EXTRA)) {
            GlobalScope.launch {
                val habit = AppDatabase.getDatabase(this@EditTodoHabit).habitDao().getHabit(intent.getSerializableExtra(ID_EXTRA) as Int)
                title.setText(habit.title)
                description.setText(habit.description)
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
            if (title.text.toString() == "") {
                Toast.makeText(this, "Title must not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            GlobalScope.launch {
                if (type == TYPE.HABIT) {
                    if (intent.hasExtra(ID_EXTRA)) {
                        AppDatabase.getDatabase(this@EditTodoHabit).habitDao().updateHabit(
                            intent.getSerializableExtra(ID_EXTRA) as Int,
                            title.text.toString(),
                            description.text.toString(),
                        )
                    } else {
                        AppDatabase.getDatabase(this@EditTodoHabit).habitDao().insertHabit(
                            Habit(
                                title.text.toString(),
                                description.text.toString(),
                                HomePage.USER_ID
                            )
                        )
                    }
                } else if (type == TYPE.TODO) {
                    if (intent.hasExtra(ID_EXTRA)) {
                        AppDatabase.getDatabase(this@EditTodoHabit).toDoDao().updateToDoWithTagCheck(
                            intent.getSerializableExtra(ID_EXTRA) as Int,
                            title.text.toString(),
                            description.text.toString(),
                            stringToDate(dateButton.text.toString()),
                            "TAG"
                        )
                    } else {
                        AppDatabase.getDatabase(this@EditTodoHabit).toDoDao().insertToDoWithTagCheck(
                            ToDo(
                                title.text.toString(),
                                description.text.toString(),
                                stringToDate(dateButton.text.toString()),
                                false, //TODO completed
                                1, //TODO tag
                                HomePage.USER_ID
                            ),
                            "TAG"
                        )
                    }
                }

                startActivity(Intent(this@EditTodoHabit, HomePage::class.java))
            }
        }

        findViewById<Button>(R.id.cancel).setOnClickListener {
            onBackPressed()
        }
    }

    fun stringToDate(date: String): Date {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.parse(date)
    }

    companion object {
        private const val TYPE_EXTRA = "type"
        private const val ID_EXTRA = "id"

        enum class TYPE {
            HABIT,
            TODO
        }

        fun newInstance(context: Context, data: TYPE, id: Int? = null): Intent {
            return Intent(context, EditTodoHabit::class.java).apply {
                putExtra(TYPE_EXTRA, data)
                if (id != null) {
                    putExtra(ID_EXTRA, id)
                }
            }
        }
    }
}
