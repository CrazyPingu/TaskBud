package com.mobile.todo.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.mobile.todo.R

class Monet {

    companion object {

        fun getColor(context: Context, defaultColor: Int, accentColor: Int): Int {
            return if (Constant.getMonet(context)) {
                accentColor
            } else {
                defaultColor
            }
        }

        fun setBorderColorMonet(context: Context): Drawable {
            val drawable = ContextCompat.getDrawable(
                context,
                R.drawable.background_card
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
    }
}