
package com.sonymobile.androidapp.moveconcept.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sonymobile.androidapp.moveconcept.service.NotificationHelper;

public class MoveReceiver extends BroadcastReceiver {

    // MoveService moveAlarm = new MoveService();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("SmartMotion", "Receiving");
        NotificationHelper.showStartMovingNotification();
        // setAlarms(context);

    }

}
