
package com.sonymobile.androidapp.moveconcept.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class CompareIdleTimeService extends Service {

    private static TimerTask timerTask = new TimerCount();

    private Context ctx;

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        startService();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private static void startService() {

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 1 * 1000);
    }
}
