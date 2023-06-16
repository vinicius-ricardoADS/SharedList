package br.edu.ifsp.ads.sharedlist.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import br.edu.ifsp.ads.sharedlist.databinding.ActivityTaskBinding
import br.edu.ifsp.ads.sharedlist.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TaskActivity : BasicActivity() {

    private val atmb: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(atmb.root)

        val receivedTask = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_TASK)
        }

        receivedTask?.let { _receivedTask ->
            with (atmb) {
                with (_receivedTask) {
                    titleEt.setText(title)
                    userEt.setText(userWhoCreated)
                    descriptionEt.setText(description)
                    dateCreatedEt.setText(dateCreation)
                    datePreviewEt.setText(datePreview)
                }
            }

            val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)
            val editTask = intent.getBooleanExtra(EXTRA_EDIT_TASK, true)

            with (atmb) {
                titleEt.isEnabled = !viewTask
                userEt.isEnabled = !editTask
                descriptionEt.isEnabled = !viewTask
                dateCreatedEt.isEnabled = !editTask
                datePreviewEt.isEnabled = !viewTask
                saveBt.visibility = if (viewTask) View.GONE else View.VISIBLE
                editBt.visibility = if (viewTask) View.GONE else View.VISIBLE
            }

            with (atmb) {
                completeBt.setOnClickListener {
                    with (_receivedTask) {
                        val task: Task = Task(
                            id = id, //operacao ternária (operador elvis)
                            title = title,
                            userWhoCreated = userWhoCreated,
                            dateCreation = dateCreation,
                            datePreview = datePreview,
                            description = description,
                            finished = true
                        )

                        val resultIntent = Intent()
                        resultIntent.putExtra(EXTRA_TASK, task)
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                }
            }

            with (atmb) {
                editBt.setOnClickListener {
                    with (_receivedTask) {
                        val task: Task = Task(
                            id = id, //operacao ternária (operador elvis)
                            title = title,
                            userWhoCreated = userWhoCreated,
                            dateCreation = dateCreation,
                            datePreview = datePreview,
                            description = description,
                            finished = finished
                        )

                        val resultIntent = Intent()
                        resultIntent.putExtra(EXTRA_TASK, task)
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                }
            }
        }

        val currentDate = LocalDate.now()
        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = currentDate.format(dateFormat)
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = firebaseAuth.currentUser

        with (atmb) {
            userEt.setText(currentUser?.email)
            dateCreatedEt.setText(formattedDate)

            saveBt.setOnClickListener{
                val task: Task = Task(
                    id = generateId(), //operacao ternária (operador elvis)
                    title = titleEt.text.toString(),
                    userWhoCreated = userEt.text.toString(),
                    dateCreation =  dateCreatedEt.text.toString(),
                    datePreview = datePreviewEt.text.toString(),
                    description = descriptionEt.text.toString(),
                    finished = false
                )

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_TASK, task)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun generateId(): Int {
        val random = Random(System.currentTimeMillis())
        return random.nextInt()
    }

}