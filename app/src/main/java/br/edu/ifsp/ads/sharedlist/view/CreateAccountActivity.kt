package br.edu.ifsp.ads.sharedlist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.ifsp.ads.sharedlist.databinding.ActivityCreateAccountBinding
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity : AppCompatActivity() {

    private val cmb: ActivityCreateAccountBinding by lazy {
        ActivityCreateAccountBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(cmb.root)

        with (cmb) {
            createAccountBt.setOnClickListener {
                val email = emailEt.text.toString()
                val password = passwordEt.text.toString()
                val repeatPassword = repeatPasswordEt.text.toString()

                if (password == repeatPassword) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@CreateAccountActivity,
                                "Usuário $email criado com sucesso!",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this@CreateAccountActivity,
                                "Erro na criação do usuário!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        this@CreateAccountActivity,
                        "Senhas não coincidem!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}