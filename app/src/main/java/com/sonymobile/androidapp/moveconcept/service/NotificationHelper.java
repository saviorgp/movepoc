
package com.sonymobile.androidapp.moveconcept.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.RemoteViews;

import com.sonymobile.androidapp.moveconcept.R;
import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;
import com.sonymobile.androidapp.moveconcept.utils.Constants;
import com.sonymobile.androidapp.moveconcept.view.MainActivity;
import com.sonymobile.androidapp.moveconcept.view.ReadyFragment;
import com.sonymobile.androidapp.moveconcept.view.TimeToMoveActivity;

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
                R.id.layout_time_to_move_notification);

        Intent notificationIntent = new Intent(context, TimeToMoveActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.setPackage(context.getPackageName());

        PendingIntent contentIntent = PendingIntent.getActivity(context, 1,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Resources res = context.getResources();

        Notification.Builder builder = new Notification.Builder(context);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        remoteViews.setCharSequence(R.id.title, "setText", res.getString(R.string.MV_INITIAL_TITLE));
        remoteViews.setCharSequence(R.id.subtitle, "setText", res.getString(R.string.MV_NOTIFICATION_SUBTITLE));
        remoteViews.setInt(R.id.notification_icon, "setImageResource", R.drawable.ic_mova_notification);

        builder.setContentIntent(contentIntent).setContent(remoteViews).setSmallIcon(R.drawable.ic_mova_notification)
                .setTicker(res.getString(R.string.MV_INITIAL_TITLE)).setVibrate(VIBRATE_START_MOVE_NOTIFICATION).setSound(uri);

        NotificationManager nm = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(Constants.NOTIFICATION_START_MOVING, builder.getNotification());

    }
}
