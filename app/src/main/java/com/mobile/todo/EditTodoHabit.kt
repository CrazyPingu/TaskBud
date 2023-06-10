package com.mobile.todo

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class EditTodoHabit : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)

        val dateButton = findViewById<Button>(R.id.date)

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
}
