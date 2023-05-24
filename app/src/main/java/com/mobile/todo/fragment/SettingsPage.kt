package com.mobile.todo.fragment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import com.mobile.todo.HomePage
import com.mobile.todo.R


class SettingsPage : Fragment() {

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
                        }
                        this@SettingsPage.resources.getString(R.string.light_mode_title) -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }
                        this@SettingsPage.resources.getString(R.string.dark_mode_title) -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
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
        fun newInstance(selectedTheme: Int) =
            SettingsPage().apply {
                arguments = Bundle().apply {
                    SELECTED_THEME =
                        if (selectedTheme == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED ||
                            selectedTheme == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        ) 0 else selectedTheme
                }
            }
    }
}