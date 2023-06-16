package br.edu.ifsp.ads.sharedlist.view

import android.content.Intent
import android.os.*
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.ifsp.ads.sharedlist.R
import br.edu.ifsp.ads.sharedlist.adapter.TaskAdapter
import br.edu.ifsp.ads.sharedlist.controller.TaskController
import br.edu.ifsp.ads.sharedlist.databinding.ActivityMainBinding
import br.edu.ifsp.ads.sharedlist.model.Task
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BasicActivity() {

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    // Data source
    private val taskList: MutableList<Task> = mutableListOf()

    // Adapter
    private val taskAdapter: TaskAdapter by lazy {
        TaskAdapter(this, taskList)
    }

    private val taskController: TaskController by lazy{
        TaskController(this)
    }

    lateinit var updateViewsHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        amb.listLv.adapter = taskAdapter

        taskController.getTasks()

        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            if (it?.resultCode == RESULT_OK) {
                val task = when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> it.data?.getParcelableExtra(EXTRA_TASK, Task::class.java)
                    else -> it.data?.getParcelableExtra<Task>(EXTRA_TASK)
                }

                task?.let { _task ->
                    //if(contactList.any{it.id == _contact.id}){ //any retorna verdaeiro se pelo menos um elemento casa com o predicado (comparacao)
                    val position = taskList.indexOfFirst { it.id == _task.id } //retorna o indice(index) caso o predicado seja satisfeito, ou seja, caso algum contato da minha lista tenha um id igual
                    if (position != -1) {
                        taskList[position] = _task
                        taskController.editTask(_task)
                        Toast.makeText(this, "Task editada!", Toast.LENGTH_LONG).show()
                    } else {
                        taskController.insertTask(_task)
                        Toast.makeText(this, "Task adicionado!", Toast.LENGTH_LONG).show()
                    }
                    taskController.getTasks()
                    taskAdapter.notifyDataSetChanged()
                }
            }
        }

        registerForContextMenu(amb.listLv)

        amb.listLv.onItemClickListener = AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
            val member = taskList[p2]
            carl.launch(Intent(this@MainActivity, TaskActivity::class.java).putExtra(EXTRA_TASK, member).putExtra(EXTRA_VIEW_TASK, true))
        }

        updateViewsHandler = Handler(Looper.myLooper()!!){ msg ->
            taskController.getTasks()
            true
        }
        updateViewsHandler.sendMessageDelayed(Message(), 3000)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addTask -> {
                carl.launch(Intent(this, TaskActivity::class.java))
                true
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                googleSignInClient.signOut()
                finish()
                true
            }
            else -> false
        }
    }

    override fun onCreateContextMenu (
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        val task = taskList[position]
        return when (item.itemId) {
            R.id.removeTaskMi -> {
                taskList.removeAt(position)
                taskController.removeTask(task)
                taskAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Task removida!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.editTaskMi -> {
                carl.launch(Intent(this, TaskActivity::class.java).putExtra(EXTRA_TASK, task).putExtra(EXTRA_EDIT_TASK, true))
                true
            }
            R.id.detailsTaskMi -> {
                carl.launch(Intent(this, TaskActivity::class.java).putExtra(EXTRA_TASK, task).putExtra(EXTRA_VIEW_TASK, true))
                true
            }
            else -> false
        }
    }

    fun updateTaskList(_contactList: MutableList<Task>){
        taskList.clear()
        taskList.addAll(_contactList)
        taskAdapter.notifyDataSetChanged()
    }
}