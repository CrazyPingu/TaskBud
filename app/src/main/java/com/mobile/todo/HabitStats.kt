package com.mobile.todo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mobile.todo.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel

class HabitStats : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habits_stats)

        val mPieChart = findViewById<PieChart>(R.id.piechart)

        // number1 is the total size of the pie.
        // number2 is the space occupied.
        val database = AppDatabase.getDatabase(this)

        GlobalScope.launch(Dispatchers.IO) {
            val completed = database.habitDao().getCompletedHabitsCount(HomePage.USER_ID).toFloat()
            val total = database.habitDao().getUserHabitsCount(HomePage.USER_ID).toFloat()

            // The rest of the pie that is not occupied.
            val notCompleted = total - completed

            Log.d("HabitStats", "completed: $completed")
            Log.d("HabitStats", "total: $total")
            Log.d("HabitStats", "notCompleted: $notCompleted")

            withContext(Dispatchers.Main) {

                if(total != 0f) {
                    // Create slices for the PieChart.
                    mPieChart.addPieSlice(
                        PieModel(
                            "Completed",
                            completed,
                            ContextCompat.getColor(this@HabitStats, R.color.piechart_completed)
                        )
                    )
                    mPieChart.addPieSlice(
                        PieModel(
                            "Not completed",
                            notCompleted,
                            ContextCompat.getColor(this@HabitStats, R.color.piechart_incompleted)
                        )
                    )

                    // Remove the inner padding to make the chart a full pie.
                    mPieChart.innerPadding = 10f

                    // Set the animation time to 500 milliseconds.
                    mPieChart.animationTime = 500

                    // Draw the pie chart.
                    mPieChart.startAnimation()

                }else{
                    findViewById<TextView>(R.id.completed).visibility = View.GONE
                    findViewById<TextView>(R.id.incompleted).visibility = View.GONE
                    findViewById<TextView>(R.id.no_data_message).visibility = View.VISIBLE
                }
            }
        }


        findViewById<Button>(R.id.button_return).setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
        }


    }
}