package com.mobile.todo.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.mobile.todo.R
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.database.dataset.ToDo
import com.mobile.todo.utils.Constant
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TodoWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return TodoWidgetFactory(applicationContext, intent!!)
    }


    class TodoWidgetFactory(private val context: Context, intent: Intent) : RemoteViewsFactory {

        private val appWidgetId: Int
        private lateinit var data: List<ToDo>

        init {
            this.appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        override fun onCreate() {
        }

        override fun onDataSetChanged() {
            val database = AppDatabase.getDatabase(context)

            val userId = Constant.getUser(context)

            data = database.toDoDao().getAllToDoByUserId(userId)
        }

        override fun onDestroy() {
            data = emptyList()
        }

        override fun getCount(): Int {
            return data.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val views = RemoteViews(context.packageName, R.layout.list_widget)

            views.setTextViewText(R.id.todo_title, data[position].title)

            return views
        }

        override fun getLoadingView(): RemoteViews {
            return RemoteViews(context.packageName, R.layout.list_widget)
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

    }
}