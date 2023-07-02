package com.mobile.todo.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mobile.todo.Login
import com.mobile.todo.R
import com.mobile.todo.utils.Constant
import com.mobile.todo.utils.Monet


class SettingsPage : Fragment() {

    private var SELECTED_THEME: Int = 0
    private lateinit var checkBox: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val spinner: Spinner = view.findViewById(R.id.theme_toggle)
        val monetZone : LinearLayout = view.findViewById(R.id.monet_zone)
        var click = false

        if (Constant.checkVersionMonet()) {
            monetZone.visibility = VISIBLE

            checkBox = view.findViewById(R.id.monet_toggle)

            checkBox.isChecked = Constant.getMonet(requireContext())

            val drawable = Monet.setBorderColorMonet(requireContext())
            view.findViewById<RelativeLayout>(R.id.title_layout).background = drawable
            monetZone.background = drawable

            Monet.setCheckBoxMonet(checkBox, requireContext())

            Monet.setButtonMonet(view.findViewById(R.id.logout), requireContext())

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                Constant.saveMonet(requireContext(), isChecked)
                requireActivity().recreate()
            }
        }
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
                            Constant.saveTheme(
                                requireContext(),
                                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                            )
                        }

                        this@SettingsPage.resources.getString(R.string.light_mode_title) -> {
                            Constant.saveTheme(
                                requireContext(),
                                AppCompatDelegate.MODE_NIGHT_NO
                            )
                        }

                        this@SettingsPage.resources.getString(R.string.dark_mode_title) -> {
                            Constant.saveTheme(
                                requireContext(),
                                AppCompatDelegate.MODE_NIGHT_YES
                            )
                        }
                    }

                    requireActivity().recreate()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Handle the case when no item is selected (optional)
                }
            }

        spinner.setSelection(SELECTED_THEME)


        view.findViewById<Button>(R.id.logout).setOnClickListener {
            Constant.logoutUser(requireContext())
            startActivity(Intent(requireContext(), Login::class.java))
        }
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