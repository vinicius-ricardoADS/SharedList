package br.edu.ifsp.ads.sharedlist.view

import android.os.Bundle
import br.edu.ifsp.ads.sharedlist.databinding.ActivityTaskBinding

class TaskActivity : BasicActivity() {

    private val atmb: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(atmb.root)
    }
}