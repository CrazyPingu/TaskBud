package com.mobile.todo.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mobile.todo.Login
import com.mobile.todo.R
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.database.dataset.Tag
import com.mobile.todo.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TodoWidget : AppWidgetProvider() {

    companion object {
        const val ACTION_TOAST = "com.mobile.todo.widget.ACTION_TOAST"
        const val SET_TO_FAV = "com.mobile.todo.widget.SET_TO_FAV"
        const val EXTRA_ITEM_ID = "com.mobile.todo.widget.EXTRA_ITEM_ID_TODO"
        const val EXTRA_ITEM_COMPLETED = "com.mobile.todo.widget.EXTRA_ITEM_ID"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (ACTION_TOAST == intent?.action && context != null) {
            val todoId = intent.getIntExtra(EXTRA_ITEM_ID, -1)


            if (todoId == -1) {
                return
            }


             GlobalScope.launch(Dispatchers.IO) {

                val database = AppDatabase.getDatabase(context)

                if (intent.hasExtra(SET_TO_FAV)) {
                    if (intent.extras?.getBoolean(SET_TO_FAV) == true) {
                        database.searchDao().removeTagFromToDoId(todoId, Tag.FAV)
                    } else {
                        database.searchDao().addTagToToDoId(todoId, Tag.FAV)
                    }
                } else {
                    val completed = intent.getBooleanExtra(EXTRA_ITEM_COMPLETED, false)
                    database.toDoDao().setCompleted(todoId, !completed)
                    Constant.refreshWidget(context)
                }

            }
        }

        super.onReceive(context, intent)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.todo_widget)

    val serviceIntent = Intent(context, TodoWidgetService::class.java)
    serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))

    val clickIntent = Intent(context, TodoWidget::class.java)
    clickIntent.action = TodoWidget.ACTION_TOAST
    val clickPendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        clickIntent,
        PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )


    // set the intent for the button to Login
    val intent = Intent(context, Login::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    views.setOnClickPendingIntent(R.id.login_button, pendingIntent)

    views.setRemoteAdapter(R.id.list_view, serviceIntent)
    views.setPendingIntentTemplate(R.id.list_view, clickPendingIntent)

    views.setViewVisibility(
        R.id.list_view,
        if (Constant.getUser(context) != -1) View.VISIBLE else View.GONE
    )
    views.setViewVisibility(
        R.id.login_button,
        if (Constant.getUser(context) == -1) View.VISIBLE else View.GONE
    )

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}