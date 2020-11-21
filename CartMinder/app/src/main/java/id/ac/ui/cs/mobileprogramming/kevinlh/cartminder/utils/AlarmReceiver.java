package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("cartId", 0);
        String cartTitle = intent.getStringExtra("cartTitle");
        String title = context.getResources().getString(R.string.notification_title);
        String message = context.getResources().getString(R.string.notification_message_prefix) + cartTitle;
        NotificationService notificationService = new NotificationService(context, id, String.valueOf(id));
        notificationService.notify(title, message);
    }
}
