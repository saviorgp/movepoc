
package com.sonymobile.androidapp.moveconcept.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.RemoteViews;

import com.sonymobile.androidapp.moveconcept.R;
import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;
import com.sonymobile.androidapp.moveconcept.utils.Constants;

public class NotificationHelper {

    /**
     * Define move notification vibration
     */
    private static final long[] VIBRATE_START_MOVE_NOTIFICATION = {
            0, 300, 300, 300
    };

    public static void showStartMovingNotification() {
        Context context = ApplicationData.getAppContext();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.id.layout_gps_lost_notification);

        Resources res = context.getResources();

        Notification.Builder builder = new Notification.Builder(context);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        remoteViews.setCharSequence(R.id.subtitle, "setText", "Move your fat ass !");

        builder.setContent(remoteViews).setSmallIcon(R.drawable.ic_launcher)
                .setTicker("Move your fat ass !").setVibrate(VIBRATE_START_MOVE_NOTIFICATION).setSound(uri);

        NotificationManager nm = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(Constants.NOTIFICATION_START_MOVING, builder.getNotification());

    }
}
