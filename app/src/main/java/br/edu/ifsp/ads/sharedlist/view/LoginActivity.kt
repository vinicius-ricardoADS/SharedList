package br.edu.ifsp.ads.sharedlist.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.ifsp.ads.sharedlist.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : BasicActivity() {

    private val alb: ActivityLoginBinding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var gsarl : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(alb.root)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val login_email = sharedPreferences.getString("email", "")

        if (!login_email.isNullOrBlank()) {
            alb.emailEt.setText(login_email)
            sharedPreferences.edit().putString("email", login_email).apply()
        }

        alb.createAccountBt.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }

        alb.passwordResetBt.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        alb.loginBt.setOnClickListener {
            val email = alb.emailEt.text.toString()
            val password = alb.passwordEt.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnSuccessListener {
                Toast.makeText(this, "Usuário $email autenticado com sucesso!", Toast.LENGTH_LONG).show()
                sharedPreferences.edit().putString("email", email).apply()
                openMainActivity()
            }.addOnFailureListener {
                Toast.makeText(this, "Falha na autenticação do usuário!", Toast.LENGTH_LONG).show()
            }
        }

        alb.loginGoogleBt.setOnClickListener{
            val gsa: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
            if (gsa == null){
                //Solicitar login com conta Google
                gsarl.launch(googleSignInClient.signInIntent)
            } else{
                //Já existe um usuário logado com conta google
                openMainActivity()
            }
        }

        gsarl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                //Pegando retorno do login com conta google
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val gsa: GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(gsa.idToken, null)

                FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener {
                    Toast.makeText(this, "Usuário ${gsa.email} autenticado com sucesso!", Toast.LENGTH_LONG).show()
                    openMainActivity()
                }.addOnFailureListener {
                    Toast.makeText(this, "Falha na autenticação de usuário!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null){
            openMainActivity()
        }
    }

    private fun openMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}