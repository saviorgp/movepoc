
package com.sonymobile.androidapp.moveconcept.service;

import android.util.Log;

import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;
import com.sonymobile.androidapp.moveconcept.persistence.SharedPreferencesHelper;

import java.util.Date;
import java.util.TimerTask;

public class TimerCount extends TimerTask {

    SharedPreferencesHelper prefs = ApplicationData.getSharedPreferences();

    @Override
    public void run() {
        Log.i("SmartMotion", "Timer task started: " + new Date());
        compareTime();
        Log.i("SmartMotion", "Timer task finished: " + new Date());

    }

    private void compareTime() {
        long teste = System.currentTimeMillis() - prefs.getStartUnmoving();
        Log.i("SmartMotion", "Comparing... " + teste + " >= 30000");
        if (System.currentTimeMillis() - prefs.getStartUnmoving() >= (30 * 1000)) {
            NotificationHelper.showStartMovingNotification();
            Log.i("SmartMotion", "Notification !!");
            prefs.setStartUnmoving(System.currentTimeMillis());
        }

    }
}
