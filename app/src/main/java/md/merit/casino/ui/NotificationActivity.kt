package md.merit.casino.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_notification.*
import md.merit.casino.R
import md.merit.casino.data.SaveData

class NotificationActivity : AppCompatActivity() {
    private lateinit var saveData: SaveData
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        saveData = SaveData(this)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null)
            startActivity(Intent(this,LoginActivity::class.java))

        tv_total_money_notif1.text = saveData.loadMoney().toString()
        tv_total_money_notif2.text = (saveData.loadMoney().plus(1000)).toString()

        btn_bonus_money.setOnClickListener {
            bonusMoney()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun bonusMoney(){
        val money = saveData.loadMoney().plus(1000)
        saveData.setMoney(money)
    }
}