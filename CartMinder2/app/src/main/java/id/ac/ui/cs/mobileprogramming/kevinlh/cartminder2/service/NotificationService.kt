package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R

class NotificationService(
    base: Context,
    private val channel_id: Int = 0,
    private val channel_name: String = ""
) :
    ContextWrapper(base) {
    private var notificationManager: NotificationManager? = null
        get() = field
            ?: (applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).also {
                field = it
            }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannel()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(
                channel_id.toString(),
                channel_name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableVibration(true)
                setShowBadge(true)
                enableLights(true)
                lightColor = Color.parseColor("#e8334a")
                description = "Reminder for carts"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
        notificationManager?.createNotificationChannel(channel)
    }

    fun notify(title: String?, message: String?) {
        val notif: Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notif = Notification.Builder(applicationContext, channel_id.toString())
                .setSmallIcon(R.drawable.ic_alarm)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(
                    Notification.BigTextStyle()
                        .bigText(message)
                )
                .setContentText(message).build()
        } else {
            notif = Notification.Builder(applicationContext)
                .setSmallIcon(R.drawable.ic_alarm)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(title)
                .setStyle(
                    Notification.BigTextStyle()
                        .bigText(message)
                )
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(message).build()
        }
        notificationManager?.notify(channel_id, notif)
    }
}