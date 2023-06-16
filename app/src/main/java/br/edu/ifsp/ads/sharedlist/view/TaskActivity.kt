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

            val viewMember = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)
            with (atmb) {
                titleEt.isEnabled = !viewMember
                userEt.isEnabled = !viewMember
                descriptionEt.isEnabled = !viewMember
                dateCreatedEt.isEnabled = !viewMember
                datePreviewEt.isEnabled = !viewMember
                saveBt.visibility = if (viewMember) View.GONE else View.VISIBLE
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
                    id = receivedTask?.id, //operacao ternária (operador elvis)
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

}