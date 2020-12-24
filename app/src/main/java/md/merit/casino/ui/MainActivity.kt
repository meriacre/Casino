package md.merit.casino.ui

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import md.merit.casino.R
import md.merit.casino.data.SaveData
import md.merit.casino.utils.ReminderBrodcast

class MainActivity : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    val CHANNNEL_ID = "notifyCasino"
    val CHANNEL_NAME = "Casino Bonuses"
    val NOTIFICATION_ID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        createNotificationChannel()
        startNotifications()

        tv_account_show.text = auth.currentUser?.email.toString()

        card_game1.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        val saveData = SaveData(this)
        tv_total_money.text = saveData.loadMoney().toString()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
           R.id.log_out -> {auth.signOut()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
        }
        return super.onOptionsItemSelected(item)
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNNEL_ID, CHANNEL_NAME, importance).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun startNotifications(){
        val intent = Intent(this, ReminderBrodcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0 , intent, 0)
        val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val currentTime = System.currentTimeMillis()
        val after10Sec = 1000 * 10 * 6 * 20 // Sent notification after 2 hours
        val after1Day = 1000 * 60 * 60 * 24 // Sent notification after 1 day

        alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime + after10Sec, pendingIntent)
        alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime + after1Day, pendingIntent)
    }



}