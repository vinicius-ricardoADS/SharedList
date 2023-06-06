package br.edu.ifsp.ads.sharedlist.view

import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.ads.sharedlist.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

sealed class BasicActivity: AppCompatActivity() {
    protected val EXTRA_TASK = "EXTRA_TASK"
}