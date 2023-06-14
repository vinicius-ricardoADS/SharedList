package br.edu.ifsp.ads.sharedlist.controller

import br.edu.ifsp.ads.sharedlist.model.Task
import br.edu.ifsp.ads.sharedlist.model.TaskDao
import br.edu.ifsp.ads.sharedlist.model.TaskDaoRbDbFb
import br.edu.ifsp.ads.sharedlist.view.MainActivity

class TaskController (private val mainActivity: MainActivity) {

    private val taskDaoImpl: TaskDao = TaskDaoRbDbFb()

    fun insertTask (task: Task) {
        Thread {
            taskDaoImpl.createTask(task)
            val list = taskDaoImpl.retrieveTasks()
            mainActivity.runOnUiThread {
                mainActivity.updateTaskList(list)
            }
        }.start()
    }

    fun getTasks() {
        Thread {
            val list = taskDaoImpl.retrieveTasks()
            mainActivity.runOnUiThread {
                mainActivity.updateTaskList(list)
            }
        }.start()
    }

    fun editTask(task: Task) {
        Thread{
            taskDaoImpl.updateTask(task)
        }.start()
    }

    fun removeTask(task: Task) {
        Thread{
            taskDaoImpl.deleteTask(task)
        }.start()
    }
}