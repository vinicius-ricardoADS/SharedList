package br.edu.ifsp.ads.sharedlist.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.edu.ifsp.ads.sharedlist.R
import java.sql.SQLException

class TaskDaoSqlite (context: Context): TaskDao {

    companion object {
        private const val TASK_DATABASE_FILE = "tasks"
        private const val TASK_TABLE = "task"
        private const val ID_COLUMN = "id"
        private const val TITLE_COLUMN = "title"
        private const val USER_COLUMN = "user"
        private const val DESCRIPTION_COLUMN = "description"
        private const val DATE_CREATED_COLUMN = "date_created"
        private const val DATE_PREVIEW_COLUMN = "date_preview"

        private const val CRATE_TASK_TABLE_STATEMENT =
            "create table if not exists $TASK_TABLE (" +
                    "$ID_COLUMN integer primary key autoincrement, " +
                    "$TITLE_COLUMN text not null, " +
                    "$USER_COLUMN text not null, " +
                    "$DESCRIPTION_COLUMN text not null, " +
                    "$DATE_CREATED_COLUMN text not null, " +
                    "$DATE_PREVIEW_COLUMN text not null, " +
                    ");"
    }

    private val taskSqliteDatabase: SQLiteDatabase

    init {
        // Criando ou abrindo o banco
        taskSqliteDatabase = context.openOrCreateDatabase(
            TASK_DATABASE_FILE,
            Context.MODE_PRIVATE, null)
        try {
            taskSqliteDatabase.execSQL(CRATE_TASK_TABLE_STATEMENT)
        } catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    override fun createTask (task: Task) {
        taskSqliteDatabase.insert(TASK_TABLE, null, task.toContentValues()).toInt()
    }

    override fun retrieveTasks(): MutableList<Task> {
        val taskList = mutableListOf<Task>()

        val cursor = taskSqliteDatabase.rawQuery("select * from $TASK_TABLE order by $TITLE_COLUMN", null)

        while (cursor.moveToFirst()) {
            taskList.add(cursor.rowToContact())
        }
        cursor.close()

        return taskList
    }

    override fun updateTask(task: Task) = taskSqliteDatabase.update(
        TASK_TABLE, task.toContentValues(), "$ID_COLUMN = ?", arrayOf(task.id.toString())
    )

    override fun deleteTask(task: Task) = taskSqliteDatabase.delete(
        TASK_TABLE, "$ID_COLUMN = ?", arrayOf(task.id.toString())
    )

    private fun Task.toContentValues () = with (ContentValues()) {
        put(TITLE_COLUMN, title)
        put(USER_COLUMN, userWhoCreated)
        put(DESCRIPTION_COLUMN, description)
        put(DATE_CREATED_COLUMN, dateCreation)
        put(DATE_PREVIEW_COLUMN, datePreview)
        this
    }

    private fun Cursor.rowToContact () = Task(
        getInt(getColumnIndexOrThrow(ID_COLUMN)),
        getString(getColumnIndexOrThrow(TITLE_COLUMN)),
        getString(getColumnIndexOrThrow(USER_COLUMN)),
        getString(getColumnIndexOrThrow(DESCRIPTION_COLUMN)),
        getString(getColumnIndexOrThrow(DATE_CREATED_COLUMN)),
        getString(getColumnIndexOrThrow(DATE_PREVIEW_COLUMN))
    )
}