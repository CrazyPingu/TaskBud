package com.mobile.todo

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class EditTodoHabit : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)

        val dateButton = findViewById<Button>(R.id.date)

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
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // Display Selected date in text view
                dateButton.text = "$dayOfMonth/${monthOfYear + 1}/$year"
            }, year, month, day)
            dpd.show() // Show DatePickerDialog
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
