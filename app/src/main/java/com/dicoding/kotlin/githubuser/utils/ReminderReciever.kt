package com.dicoding.kotlin.githubuser.utils

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dicoding.kotlin.githubuser.R
import com.dicoding.kotlin.githubuser.activity.MainActivity
import java.util.*

class ReminderReciever: BroadcastReceiver() {

    val title = "Reminder"
    val message = "It's time to dive into ocean of codes on GitHub!"
    val notifId = ID_REMINDER

    override fun onReceive(context: Context, intent: Intent) {
        showReminderNotification(context)
    }

    fun setReminderAlarm(context: Context) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReciever::class.java)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REMINDER, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReciever::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REMINDER, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }

    private fun showReminderNotification(context: Context) {

        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager channel"

        val notifReminderIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context)
            .addParentStack(MainActivity::class.java)
            .addNextIntent(notifReminderIntent)
            .getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notifications_black_24)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        //Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create or update
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManagerCompat.notify(notifId, notification)
    }

    companion object{
        private const val ID_REMINDER = 100
    }
}