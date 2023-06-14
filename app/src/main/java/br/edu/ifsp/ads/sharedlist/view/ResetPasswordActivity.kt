package br.edu.ifsp.ads.sharedlist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.ifsp.ads.sharedlist.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : BasicActivity() {

    private val rmb: ActivityResetPasswordBinding by lazy {
        ActivityResetPasswordBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(rmb.root)

        with (rmb) {
            sendEmailBt.setOnClickListener {
                val email = recoveryPasswordEmailEt.text.toString()
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { resultado ->
                    if (resultado.isSuccessful) {
                        Toast.makeText(this@ResetPasswordActivity, "Email de recuperação enviado para $email", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this@ResetPasswordActivity, "Falha no envio do email de recuperação!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}