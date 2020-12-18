package md.merit.casino.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import md.merit.casino.R
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_game.*
import md.merit.casino.models.Game1

class GameActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tv_total_money.text = "1500"

    btn_spin.setOnClickListener {
        val game1 = Game1()
        val ob = game1.spinIt(this,iv_slot1, iv_slot2, iv_slot3)
        game1.sumIt(tv_total_money,tv_bid,ob)
        Log.d("Sloturi", "slot1: ${ob.slot1}, slot2:${ob.slot2} slot3: ${ob.slot3}")
    }
    }


}