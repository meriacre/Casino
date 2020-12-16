package md.merit.casino.ui

import android.os.Bundle
import md.merit.casino.R
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_game.*
import md.merit.casino.models.Game1

class GameActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


    btn_spin.setOnClickListener {
        val game1 = Game1()
        game1.spinIt(this,iv_slot1, iv_slot2, iv_slot3)
    }
    }
}