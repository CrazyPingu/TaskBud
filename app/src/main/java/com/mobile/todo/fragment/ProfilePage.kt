package com.mobile.todo.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.todo.Camera
import com.mobile.todo.HomePage
import com.mobile.todo.R
import com.mobile.todo.adapter.BadgeAdapter
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.database.dataset.Badge
import com.mobile.todo.utils.Constant
import com.mobile.todo.utils.Permission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import java.io.IOException
import java.io.InputStream


class ProfilePage : Fragment() {

    private var PROFILE_PIC_IMAGE: Uri = Uri.EMPTY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val mPieChart = view.findViewById<PieChart>(R.id.piechart)

        val recyclerView = view.findViewById<RecyclerView>(R.id.zone_badge)

        val database = AppDatabase.getDatabase(requireContext())

        GlobalScope.launch {
            // Variable required for the pie chart
            val completed = database.habitDao().getCompletedHabitsCount(HomePage.USER_ID).toFloat()
            val total = database.habitDao().getUserHabitsCount(HomePage.USER_ID).toFloat()
            // The rest of the pie that is not occupied.
            val notCompleted = total - completed


            ////////////////////////////////////////////////////////////
            // Badge section

            // All habits!
            if(database.habitDao().areAllHabitsCompleted(HomePage.USER_ID)){
                database.badgeDao().obtainedBadge(HomePage.USER_ID, Badge.allHabits.name)
            }

            // favourite
            if(database.tagDao().usedFavouriteTag(HomePage.USER_ID)){
                database.badgeDao().obtainedBadge(HomePage.USER_ID, Badge.favourite.name)
            }

            // habit streak
            if(database.habitDao().badgeHabitStreak(HomePage.USER_ID)){
                database.badgeDao().obtainedBadge(HomePage.USER_ID, Badge.habitStreak.name)
            }

            // todo streak
            if(database.toDoDao().badgeTodoStreak(HomePage.USER_ID)){
                database.badgeDao().obtainedBadge(HomePage.USER_ID, Badge.todoStreak.name)
            }

            // nice shot
            if(database.userDao().getProfilePic(HomePage.USER_ID) != Constant.DEFAULT_PROFILE_PIC){
                database.badgeDao().obtainedBadge(HomePage.USER_ID, Badge.niceShot.name)
            }




            val badge = database.badgeDao().getAllBadgeFromUser(HomePage.USER_ID)



            ////////////////////////////////////////////////////////////
            // Profile Pic section
            val user = database.userDao().getUser(HomePage.USER_ID)
            var profilePicUri = user.profilePic
            if (PROFILE_PIC_IMAGE != Uri.EMPTY) {
                AppDatabase.getDatabase(requireContext()).userDao().updateProfilePic(
                    HomePage.USER_ID,
                    PROFILE_PIC_IMAGE.toString()
                )
                profilePicUri = PROFILE_PIC_IMAGE
            } else {
                // obtain the image URI
                try {
                    // Search for profile pic
                    val inputStream: InputStream? =
                        context?.contentResolver?.openInputStream(profilePicUri)
                    inputStream?.close()
                } catch (e: IOException) {
                    // Case profile pic is not found
                    AppDatabase.getDatabase(requireContext()).userDao().updateProfilePic(
                        HomePage.USER_ID,
                        Constant.DEFAULT_PROFILE_PIC.toString()
                    )
                    profilePicUri = Constant.DEFAULT_PROFILE_PIC
                }
            }


            // Switch to the main (UI) thread to update the ImageView
            launch(Dispatchers.Main) {

                // Pie chart
                if (total != 0f) {
                    // Create slices for the PieChart.
                    mPieChart.addPieSlice(
                        PieModel(
                            "Completed",
                            completed,
                            ContextCompat.getColor(requireContext(), R.color.piechart_completed)
                        )
                    )
                    mPieChart.addPieSlice(
                        PieModel(
                            "Not completed",
                            notCompleted,
                            ContextCompat.getColor(requireContext(), R.color.piechart_incompleted)
                        )
                    )

                    // Remove the inner padding to make the chart a full pie.
                    mPieChart.innerPadding = 10f

                    // Set the animation time to 500 milliseconds.
                    mPieChart.animationTime = 500

                    // Draw the pie chart.
                    mPieChart.startAnimation()

                } else {
                    view.findViewById<TextView>(R.id.completed).visibility = View.GONE
                    view.findViewById<TextView>(R.id.incompleted).visibility = View.GONE
                    view.findViewById<TextView>(R.id.no_data_message).visibility = View.VISIBLE
                }

                // Badge
                recyclerView.adapter = BadgeAdapter(badge.toMutableList())
                recyclerView.layoutManager = LinearLayoutManager(context)

                // Profile Pic
                view.findViewById<ImageView>(R.id.profile_pic).setImageURI(profilePicUri)
                view.findViewById<TextView>(R.id.username).text = user.username

                view.findViewById<ImageView>(R.id.profile_pic).setOnClickListener {
                    redirectToCamera(profilePicUri)
                }
            }
        }



        return view
    }

    private fun redirectToCamera(profilePicUri: Uri = Constant.DEFAULT_PROFILE_PIC) {
        val intent = Intent(context, Camera::class.java)
        intent.putExtra("profilePic", profilePicUri)
        Camera.PAGE_TO_RETURN = HomePage::class
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Permission.CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    redirectToCamera()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        fun newInstance(profilePicImage: Uri = Uri.EMPTY) =
            ProfilePage().apply {
                arguments = Bundle().apply {
                    PROFILE_PIC_IMAGE = profilePicImage
                }
            }
    }
}