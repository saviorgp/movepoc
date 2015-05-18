
/*
 * Copyright (C) 2015 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */

package com.sonymobile.androidapp.moveconcept.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sonymobile.androidapp.moveconcept.service.NotificationHelper;

/**
 * @author vntgago
 * @created 15/05/2015
 */

public class MoveReceiver extends BroadcastReceiver {

    // MoveService moveAlarm = new MoveService();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("SmartMotion", "Receiving");
        NotificationHelper.showStartMovingNotification();
        // setAlarms(context);

    }

}
