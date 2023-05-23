package com.mobile.todo.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import com.mobile.todo.R
import kotlin.properties.Delegates


class SettingsPage : Fragment() {

    private var USER_ID: Int = 0
    private lateinit var ACTIVITY: AppCompatActivity
    private var SELECTED_THEME: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val spinner: Spinner = view.findViewById(R.id.theme_toggle)
        var click = false

        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Remove the first click to prevent the default theme from being selected
                    if (!click) {
                        click = true
                        return
                    }
                    when (parent.getItemAtPosition(position).toString()) {
                        this@SettingsPage.resources.getString(R.string.system_title) -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                            recreate(ACTIVITY)
                        }
                        this@SettingsPage.resources.getString(R.string.light_mode_title) -> {
                            // Action for Light Theme
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            recreate(ACTIVITY)
                        }
                        this@SettingsPage.resources.getString(R.string.dark_mode_title) -> {
                            // Action for Dark Theme
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            recreate(ACTIVITY)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Handle the case when no item is selected (optional)
                }
            }

        spinner.setSelection(SELECTED_THEME)
        return view
    }


    companion object {
        fun newInstance(idUser: Int, activity: AppCompatActivity, selectedTheme: Int) =
            SettingsPage().apply {
                arguments = Bundle().apply {
                    USER_ID = idUser
                    ACTIVITY = activity
                    SELECTED_THEME =
                        if (selectedTheme == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED ||
                            selectedTheme == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) 0 else selectedTheme
                }
            }
    }
}