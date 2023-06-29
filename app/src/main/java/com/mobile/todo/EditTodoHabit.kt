package com.mobile.todo

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.database.dataset.Habit
import com.mobile.todo.database.dataset.Search
import com.mobile.todo.database.dataset.Tag
import com.mobile.todo.database.dataset.ToDo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
            findViewById<CheckBox>(R.id.starCheckBoxEdit).visibility = View.GONE
        }

        if (intent.hasExtra(ID_EXTRA)) {
            GlobalScope.launch {
                val habit = AppDatabase.getDatabase(this@EditTodoHabit).habitDao()
                    .getHabit(intent.getSerializableExtra(ID_EXTRA) as Int)
                val todo = AppDatabase.getDatabase(this@EditTodoHabit).toDoDao()
                    .getToDoById(intent.getSerializableExtra(ID_EXTRA) as Int)

                if (type == TYPE.HABIT) {
                    title.setText(habit.title)
                    description.setText(habit.description)
                } else if (type == TYPE.TODO) {
                    if (todo != null) {
                        title.setText(todo.title)
                        description.setText(todo.description)
                        val tags = AppDatabase.getDatabase(this@EditTodoHabit).searchDao()
                            .getTagsByToDoId(intent.getSerializableExtra(ID_EXTRA) as Int)
                        if (todo.date != null)
                            dateButton.text = dateToString(todo.date)
                        if (tags.contains(Tag.FAV)) {
                            findViewById<CheckBox>(R.id.starCheckBoxEdit).isChecked = true
                        }
                        findViewById<EditText>(R.id.tag).setText(tags.filter { tag -> tag != Tag.FAV }
                            .joinToString(" "))

                    }
                }
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
                        // Update to do
                        AppDatabase.getDatabase(this@EditTodoHabit).toDoDao().updateToDo(
                            intent.getSerializableExtra(ID_EXTRA) as Int,
                            title.text.toString(),
                            description.text.toString(),
                            stringToDate(dateButton.text.toString())
                        )

                        // Update search table with tags related to the to do
                        AppDatabase.getDatabase(this@EditTodoHabit).searchDao().updateTagsForToDoId(
                            intent.getSerializableExtra(ID_EXTRA) as Int,
                            AppDatabase.getDatabase(this@EditTodoHabit).searchDao()
                                .getTagsByToDoId(intent.getSerializableExtra(ID_EXTRA) as Int),
                        )

                        // Add to search table with tag fav if the star checkbox is checked
                        if (findViewById<CheckBox>(R.id.starCheckBoxEdit).isChecked) {
                            AppDatabase.getDatabase(this@EditTodoHabit).searchDao().insertSearch(
                                Search(
                                    AppDatabase.getDatabase(this@EditTodoHabit).toDoDao()
                                        .getLastInsertedId()!!,
                                    Tag.FAV
                                )
                            )
                        } else {
                            AppDatabase.getDatabase(this@EditTodoHabit).searchDao()
                                .removeTagFromToDoId(
                                    intent.getSerializableExtra(ID_EXTRA) as Int,
                                    Tag.FAV
                                )
                        }

                        // Get a list of all tags in lowercase and
                        val tagArray = tag.text.toString().toLowerCase()
                            .split(" ")
                            .distinct()
                            .filter { it.isNotBlank() }

                        // Remove all tags linked to the to do id
                        AppDatabase.getDatabase(this@EditTodoHabit).searchDao().removeByToDoId(
                            AppDatabase.getDatabase(this@EditTodoHabit).toDoDao()
                                .getLastInsertedId()!!
                        )

                        // Add each tag to the tag table and to the search table linked to the to do id
                        tagArray.forEach { tag ->
                            AppDatabase.getDatabase(this@EditTodoHabit).tagDao().insertTag(
                                Tag(tag)
                            )

                            AppDatabase.getDatabase(this@EditTodoHabit).searchDao().insertSearch(
                                Search(
                                    AppDatabase.getDatabase(this@EditTodoHabit).toDoDao()
                                        .getLastInsertedId()!!,
                                    tag
                                )
                            )
                        }
                    } else {
                        // Add to do
                        AppDatabase.getDatabase(this@EditTodoHabit).toDoDao().insertToDo(
                            ToDo(
                                title.text.toString(),
                                description.text.toString(),
                                if (dateButton.text.toString() == "Select date") null else
                                stringToDate(dateButton.text.toString()),
                                false,
                                HomePage.USER_ID
                            )
                        )

                        // Add to search table with tag fav if the star checkbox is checked
                        if (findViewById<CheckBox>(R.id.starCheckBoxEdit).isChecked) {
                            AppDatabase.getDatabase(this@EditTodoHabit).searchDao().insertSearch(
                                Search(
                                    AppDatabase.getDatabase(this@EditTodoHabit).toDoDao()
                                        .getLastInsertedId()!!,
                                    Tag.FAV
                                )
                            )
                        }

                        // Get a list of all tags in lowercase and
                        // add each tag to the tag table and to the search table linked to the to do id
                        val tagArray = tag.text.toString().toLowerCase()
                            .split(" ")
                            .distinct()
                            .filter { it.isNotBlank() }

                        tagArray.forEach { tag ->
                            AppDatabase.getDatabase(this@EditTodoHabit).tagDao().insertTag(
                                Tag(tag)
                            )

                            AppDatabase.getDatabase(this@EditTodoHabit).searchDao().insertSearch(
                                Search(
                                    AppDatabase.getDatabase(this@EditTodoHabit).toDoDao()
                                        .getLastInsertedId()!!,
                                    tag
                                )
                            )
                        }
                    }
                }

                startActivity(Intent(this@EditTodoHabit, HomePage::class.java))
            }
        }

        findViewById<Button>(R.id.cancel).setOnClickListener {
            onBackPressed()
        }
    }

    private fun stringToDate(date: String): Date {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.parse(date)
    }

    private fun dateToString(date: Date): String {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(date)
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
