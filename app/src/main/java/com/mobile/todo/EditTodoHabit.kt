package com.mobile.todo

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.mobile.todo.utils.Constant
import com.mobile.todo.utils.Monet
import kotlinx.coroutines.Dispatchers
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
        val favouriteCheckbox = findViewById<CheckBox>(R.id.starCheckBoxEdit)

        val confirmButton = findViewById<Button>(R.id.confirm)
        val cancelButton = findViewById<Button>(R.id.cancel)

        val database = AppDatabase.getDatabase(this)

        val type = intent.getSerializableExtra(TYPE_EXTRA) as TYPE
        if (type == TYPE.HABIT) {
            dateButton.visibility = View.GONE
            tag.visibility = View.GONE
            favouriteCheckbox.visibility = View.GONE
        }

        if (Constant.getMonet(this)) {
            Monet.setStatusBarMonet(this, window)
            Monet.setEditText(title, this)

            description.background = Monet.setBorderColorMonet(this, R.drawable.background_square)
            description.textCursorDrawable = Monet.setCursorMonet(this)

            tag.background = Monet.setBorderColorMonet(this, R.drawable.background_square)
            tag.textCursorDrawable = Monet.setCursorMonet(this)

            Monet.setButtonMonet(confirmButton, this)
            Monet.setButtonMonet(cancelButton, this)
            Monet.setButtonMonet(dateButton, this)

            Monet.setCheckBoxMonet(favouriteCheckbox, this)
        }

        if (intent.hasExtra(ID_EXTRA)) {
            GlobalScope.launch(Dispatchers.IO) {
                val habit = database.habitDao()
                    .getHabit(intent.getSerializableExtra(ID_EXTRA) as Int)
                val todo = database.toDoDao()
                    .getToDoById(intent.getSerializableExtra(ID_EXTRA) as Int)

                if (type == TYPE.HABIT) {
                    title.setText(habit.title)
                    description.setText(habit.description)
                } else if (type == TYPE.TODO) {
                    if (todo != null) {
                        title.setText(todo.title)
                        description.setText(todo.description)
                        val tags = database.searchDao()
                            .getTagsByToDoId(intent.getSerializableExtra(ID_EXTRA) as Int)
                        if (todo.date != null)
                            dateButton.text = dateToString(todo.date)
                        if (tags.contains(Tag.FAV)) {
                            favouriteCheckbox.isChecked = true
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

        confirmButton.setOnClickListener {
            if (title.text.toString() == "") {
                Toast.makeText(this, "Title must not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

             GlobalScope.launch(Dispatchers.IO) {
                if (type == TYPE.HABIT) {
                    if (intent.hasExtra(ID_EXTRA)) {
                        database.habitDao().updateHabit(
                            intent.getSerializableExtra(ID_EXTRA) as Int,
                            title.text.toString(),
                            description.text.toString(),
                        )
                    } else {
                        database.habitDao().insertHabit(
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
                        val id = intent.getSerializableExtra(ID_EXTRA) as Int

                        // Remove all tags linked to the to do id
                        database.searchDao().removeByToDoId(
                            database.toDoDao()
                                .getLastInsertedId()!!
                        )

                        database.toDoDao().updateToDo(
                            id,
                            title.text.toString(),
                            description.text.toString(),
                            stringToDate(dateButton.text.toString())
                        )

                        // Get a list of all tags in lowercase and
                        val tagArray = tag.text.toString().toLowerCase()
                            .split(" ")
                            .distinct()
                            .filter { it.isNotBlank() }


                        // Add each tag to the tag table and to the search table linked to the to do id
                        tagArray.forEach { tag ->
                            database.tagDao().insertTag(
                                Tag(tag)
                            )

                            database.searchDao().insertSearch(
                                Search(
                                    id,
                                    tag
                                )
                            )
                        }

                        // Add to search table with tag fav if the star checkbox is checked
                        if (findViewById<CheckBox>(R.id.starCheckBoxEdit).isChecked) {
                            database.searchDao().insertSearch(
                                Search(
                                    id,
                                    Tag.FAV
                                )
                            )
                        } else {
                            database.searchDao()
                                .removeTagFromToDoId(
                                    id,
                                    Tag.FAV
                                )
                        }
                    } else {
                        // Add to do
                        database.toDoDao().insertToDo(
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
                            database.searchDao().insertSearch(
                                Search(
                                    database.toDoDao()
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
                            database.tagDao().insertTag(
                                Tag(tag)
                            )

                            database.searchDao().insertSearch(
                                Search(
                                    database.toDoDao()
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

        cancelButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun stringToDate(date: String): Date? {
        if (date == "Select date") return null
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
