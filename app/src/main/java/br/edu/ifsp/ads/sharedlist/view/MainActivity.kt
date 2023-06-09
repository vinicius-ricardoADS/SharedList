package br.edu.ifsp.ads.sharedlist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import br.edu.ifsp.ads.sharedlist.R
import br.edu.ifsp.ads.sharedlist.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BasicActivity() {

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                googleSignInClient.signOut()
                finish()
                true
            }
            else -> false
        }
    }
}