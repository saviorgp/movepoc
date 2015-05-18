/*
 * Copyright (C) 2015 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */

package com.sonymobile.androidapp.moveconcept.liveware.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sonymobile.androidapp.moveconcept.liveware.service.MoveExtensionService;

/**
 * MoveExtensionReceiver
 * Created by vntgago on 24/04/2015.
 */

public class MoveExtensionReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("SmartMotion", "onReceive: " + intent.getAction());
        intent.setClass(context, MoveExtensionService.class);
        context.startService(intent);
    }
}
