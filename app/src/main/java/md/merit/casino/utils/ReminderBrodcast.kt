package md.merit.casino.utils

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import md.merit.casino.R
import md.merit.casino.ui.NotificationActivity

class ReminderBrodcast : BroadcastReceiver() {

    override fun onReceive(p0: Context, p1: Intent?) {

        val intent = Intent(p0, NotificationActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(p0).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val builder = NotificationCompat.Builder(p0, "notifyCasino")
            .setSmallIcon(R.drawable.fig1)
            .setContentTitle("Casino Bonus")
            .setContentText("Click here to take your bonus!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(p0)
        notificationManager.notify(200, builder.build())
    }
}