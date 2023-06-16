package br.edu.ifsp.ads.sharedlist.view

import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.ads.sharedlist.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

sealed class BasicActivity: AppCompatActivity() {
    protected val EXTRA_TASK = "EXTRA_TASK"
    protected val EXTRA_EDIT_TASK = "EXTRA_EDIT_TASK"
    protected val EXTRA_CREATE_TASK = "EXTRA_CREATE_TASK"
    protected val EXTRA_VIEW_TASK = "EXTRA_VIEW_TASK"

    protected val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    protected val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(this, googleSignInOptions)
    }
}