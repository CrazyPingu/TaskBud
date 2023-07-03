package com.mobile.todo.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.google.android.material.textfield.TextInputLayout
import com.mobile.todo.R


class Monet {

    companion object {

        private fun getColor(context: Context, defaultColor: Int, accentColor: Int): Int {
            return if (Constant.getMonet(context)) {
                accentColor
            } else {
                defaultColor
            }
        }

        fun setDeleteButtonMonet(imageView: ImageView, context: Context) {
            imageView.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    getColor(
                        context,
                        R.color.lavender,
                        android.R.color.system_accent2_500
                    )
                )
            )
        }

        fun setBorderColorMonet(context: Context, drawableInt : Int = R.drawable.background_card): Drawable {
            val drawable = ContextCompat.getDrawable(
                context,
                drawableInt
            ) as GradientDrawable

            drawable.setStroke(
                context.resources.getDimensionPixelSize(R.dimen.width_stroke_card),
                ContextCompat.getColor(
                    context,
                    getColor(
                        context,
                        R.color.lavender,
                        android.R.color.system_accent2_500
                    )
                )
            )
            return drawable
        }

        fun setCursorMonet(context: Context): Drawable {
            val drawable = ContextCompat.getDrawable(
                context,
                R.drawable.cursor
            ) as GradientDrawable

            drawable.setStroke(
                context.resources.getDimensionPixelSize(R.dimen.width_stroke_card),
                ContextCompat.getColor(
                    context,
                    getColor(
                        context,
                        R.color.lavender,
                        android.R.color.system_accent2_500
                    )
                )
            )
            return drawable
        }

        fun setTextInputLayoutMonet(
            layout: TextInputLayout,
            context: Context,
            isPassword: Boolean = false
        ) {
            layout.boxStrokeColor =
                ContextCompat.getColor(context, android.R.color.system_accent3_500)
            layout.hintTextColor =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        getColor(
                            context,
                            R.color.lavender,
                            android.R.color.system_accent2_500
                        )
                    )
                )

            layout.defaultHintTextColor = ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    getColor(
                        context,
                        R.color.inverse,
                        R.color.inverse,
                    )
                )
            )

            if (isPassword) {
                layout.setEndIconTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            getColor(
                                context,
                                R.color.lavender,
                                android.R.color.system_accent2_500
                            )
                        )
                    )
                )
            }
        }

        fun setButtonMonet(button: Button, context: Context) {
            button.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        getColor(
                            context,
                            R.color.lavender,
                            android.R.color.system_accent2_500
                        )
                    )
                )
        }

        fun setCheckBoxMonet(checkBox: CheckBox, context: Context) {
            checkBox.buttonTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        getColor(
                            context,
                            R.color.lavender,
                            android.R.color.system_accent2_500
                        )
                    )
                )
        }

        fun setStatusBarMonet(context: Context, window: Window) {
            window.statusBarColor =
                ContextCompat.getColor(context, android.R.color.system_accent1_500)
        }

        fun setSearchBarMonet(searchBar: SearchBar, context: Context) {
            searchBar.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    android.R.color.system_accent2_500
                )
            )
        }

        fun setFabMonet(fab: ExtendedFloatingActionButton, context: Context) {
            fab.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    android.R.color.system_accent3_500
                )
            )

            fab.rippleColor = ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    android.R.color.system_accent1_500
                )
            )
        }

        fun setEditText(editText: EditText, context: Context) {
            editText.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    android.R.color.system_accent2_500
                )
            )

            editText.textCursorDrawable = setCursorMonet(context)
        }

        fun setButtonTextMonet(button: Button, context: Context) {
            button.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        getColor(
                            context,
                            R.color.lavender,
                            android.R.color.system_accent2_500
                        )
                    )
                )
            )
        }

        fun setSearchViewMonet(searchView: SearchView) : SearchView {
            val context: Context = searchView.context
            val themedContext: Context = ContextThemeWrapper(context, R.style.SearchViewWithAccent)

            val newSearchView = SearchView(themedContext)
            val parent: ViewGroup = searchView.parent as ViewGroup
            val index: Int = parent.indexOfChild(searchView)
            parent.removeView(searchView)
            parent.addView(newSearchView, index)

            return newSearchView
        }

    }
}