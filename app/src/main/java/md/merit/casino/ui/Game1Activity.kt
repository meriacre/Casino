package md.merit.casino.ui

import android.os.Bundle
import android.util.Log
import md.merit.casino.R
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_game.*
import md.merit.casino.data.FirestoreDB
import md.merit.casino.data.SaveData
import md.merit.casino.models.Game1

class Game1Activity : FragmentActivity() {

    private lateinit var saveData: SaveData
    private lateinit var saveSlotsToFirestore: FirestoreDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        saveData = SaveData(this)
        saveSlotsToFirestore = FirestoreDB()

        tv_total_money_game1.text = saveData.loadMoney().toString()

        btn_spin.setOnClickListener {
            val game1 = Game1()
            val ob = game1.spinIt(this, iv_slot1, iv_slot2, iv_slot3)
            game1.sumIt(tv_total_money_game1, tv_bid, ob)
            Log.d("Sloturi", "slot1: ${ob.slot1}, slot2:${ob.slot2} slot3: ${ob.slot3}")
            saveData.setMoney(tv_total_money_game1.text.toString())
            saveSlotsToFirestore.saveSlots(this, ob)
            saveSlotsToFirestore.saveMoney(this)
        }

        btn_sub_bid.setOnClickListener {
            var bidul = tv_bid.text.toString().toInt()
            if (bidul >= 40) {
                bidul = bidul - 20
            }
            tv_bid.text = bidul.toString()
        }

        btn_add_bid.setOnClickListener {
            var bidul = tv_bid.text.toString().toInt()
            if (bidul <= tv_total_money_game1.text.toString().toInt()) {
                bidul = bidul + 20
            }
            tv_bid.text = bidul.toString()


        }
    }


}