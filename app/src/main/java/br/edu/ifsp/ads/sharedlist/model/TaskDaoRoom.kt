package br.edu.ifsp.ads.sharedlist.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class  TaskDaoRoom: RoomDatabase() {

    companion object {
        const val TASK_DATABASE_FILE = "tasks_room"
    }

    abstract fun getMemberDao (): TaskDao
}