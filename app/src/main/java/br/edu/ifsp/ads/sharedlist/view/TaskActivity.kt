package br.edu.ifsp.ads.sharedlist.view

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

        val receivedMember = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_TASK)
        }

        val currentDate = LocalDate.now()
        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = currentDate.format(dateFormat)
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = firebaseAuth.currentUser

        with (atmb) {
            if (currentUser != null) userEt.setText(currentUser.email)
            dateCreatedEt.setText(formattedDate)

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
    }
}