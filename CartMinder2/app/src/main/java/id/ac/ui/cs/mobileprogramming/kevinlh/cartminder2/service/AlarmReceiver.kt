package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.service

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.Cart
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id: Int = intent.getIntExtra(CHANNEL_ID, DEFAULT_CHANNEL)
        val title: String = intent.getStringExtra(RECEIVER_TITLE) ?: "cart#none"
        val body: String = intent.getStringExtra(RECEIVER_BODY) ?: "empty message"
        val notificationService = NotificationService(context, id, id.toString())
        notificationService.notify(title, body)
    }

    companion object {
        const val DEFAULT_CHANNEL = 777
        const val CHANNEL_ID = "0"
        const val RECEIVER_TITLE = "1"
        const val RECEIVER_BODY = "2"

        fun buildPendingIntent(context: Context, cart: Cart, total: Int): PendingIntent {
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra(CHANNEL_ID, cart.id.toInt())
                putExtra(
                    RECEIVER_TITLE,
                    "${context.resources.getString(R.string.notification_title)} ${cart.title}"
                )
                putExtra(
                    RECEIVER_BODY,
                    "Lets go shop at ${
                        cart.category.name.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)
                    }\nDont forget to bring IDR $total"
                )
            }
            return PendingIntent.getBroadcast(context, cart.id.toInt(), intent, 0)
        }

        fun buildPCancellingPendingIntent(context: Context, cart: Cart): PendingIntent {
            val intent = Intent(context, AlarmReceiver::class.java)
            return PendingIntent.getBroadcast(context, cart.id.toInt(), intent, 0)
        }
    }
}