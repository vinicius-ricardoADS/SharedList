package br.edu.ifsp.ads.sharedlist.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class TaskDaoRbDbFb: TaskDao {

    private val TASK_LIST_ROOT_NODE = "tasks"

    private val taskRtDbFbReference = Firebase.database.getReference(TASK_LIST_ROOT_NODE)

    //Simula uma consulta no banco de dados
    private val taskList: MutableList<Task> = mutableListOf()

    init{
        taskRtDbFbReference.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val task: Task? = snapshot.getValue<Task>()
                task?.let{ _task ->
                    if(!taskList.any{ _task.id == it.id}){
                        taskList.add(task)
                    }
                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val task: Task? = snapshot.getValue<Task>()
                task?.let{_task ->
                    taskList[taskList.indexOfFirst{ _task.id == it.id }] = _task
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val task: Task? = snapshot.getValue<Task>()
                task?.let{_task ->
                    taskList.remove(_task)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //Não se aplica
            }

            override fun onCancelled(error: DatabaseError) {
                //Não se aplica
            }
        })
        taskRtDbFbReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskHashMap = snapshot.getValue<HashMap<String, Task>>()
                taskList.clear()
                taskHashMap?.values?.forEach {
                    taskList.add(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Não se aplica
            }

        })
    }


    override fun createTask(task: Task) {
        createOrUpdateTask(task)
    }

    override fun retrieveTasks(): MutableList<Task> {
        return taskList
    }

    override fun updateTask(task: Task): Int {
        createOrUpdateTask(task)
        return 1
    }

    override fun deleteTask(task: Task): Int {
        taskRtDbFbReference.child(task.id.toString()).removeValue()
        return 1
    }

    private fun createOrUpdateTask (task: Task) = taskRtDbFbReference.child(task.id.toString()).setValue(task)
}