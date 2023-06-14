package br.edu.ifsp.ads.sharedlist.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            if (it?.resultCode == RESULT_OK) {
                val task = when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> it.data?.getParcelableExtra(EXTRA_TASK, Task::class.java)
                    else -> it.data?.getParcelableExtra<Task>(EXTRA_TASK)
                }
            }
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
        return when (item.itemId) {
            R.id.removeTaskMi -> {
                true
            }
            R.id.editTaskMi -> {
                true
            }
            R.id.detailsTaskMi -> {
                true
            }
            else -> false
        }
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

    fun updateTaskList(_contactList: MutableList<Task>){
        taskList.clear()
        taskList.addAll(_contactList)
        taskAdapter.notifyDataSetChanged()
    }
}