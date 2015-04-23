
/*
 * *
 *  * @file ApplicationData.java
 *  * @author Gabriel Goncalves (gabriel.goncalves@venturus.org.br)
 *  * @created 14/04/2015
 *
 */

package com.sonymobile.androidapp.moveconcept.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;
import com.sonymobile.androidapp.moveconcept.persistence.SharedPreferencesHelper;
import com.sonymobile.androidapp.moveconcept.receiver.MoveReceiver;
import com.sonymobile.androidapp.moveconcept.utils.Constants;

import java.util.HashSet;
import java.util.Set;

public class MoveService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;

    private Sensor mSensorAccelerometer;

    private SharedPreferencesHelper prefs;

    private long lastUpdate = 0;

    private float lastx, lasty, lastz;

    private static final int SHAKE_THRESHOLD = 400;

    private long mLastShake = 0;

    private boolean mAlarmUp = false;

    final public static String ONE_TIME = "onetime";

    private static Set<MoveListener> mListeners = new HashSet<MoveListener>();

    /**
     * Binder
     */
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        /**
         * @return service instance
         */
        public MoveService getService() {
            return MoveService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public MoveService() {
        Log.i("SmartMotion", "NEW SERVICE");
    }

    @Override
    public void onCreate() {
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // setAlarms(this);
        Log.i("SmartMotion", "INSTAnCE");
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                prefs = ApplicationData.getSharedPreferences();

                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - lastx - lasty - lastz) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    if (mLastShake == 0) {
                        mLastShake = System.currentTimeMillis();
                    }
                    prefs.setStartUnmoving(mLastShake);
                    Log.i("SmartMotion", "Set: "
                            + ApplicationData.getSharedPreferences().getStartUnmoving());
                    mLastShake = System.currentTimeMillis();
                    // mAlarmMove.cancelAlarms(ApplicationData.getAppContext());
                    setAlarms(ApplicationData.getAppContext());
                }

                lastx = x;
                lasty = y;
                lastz = z;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setAlarms(Context context) {
        AlarmManager moveAlarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Constants.START_MOVE_ALARM);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                Constants.SCHEDULE_ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        moveAlarm.set(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + Constants.IDLE_LIMIT),
                pendingIntent);
        notifyMovement(System.currentTimeMillis() + Constants.IDLE_LIMIT);
        mAlarmUp = true;
        Log.i("SmartMotion", "Setting Alarm");

    }

    public void cancelAlarms(Context context) {
        AlarmManager moveAlarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Constants.START_MOVE_ALARM);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                Constants.SCHEDULE_ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        moveAlarm.cancel(pendingIntent);
        mAlarmUp = false;
        Log.i("SmartMotion", "RemovingAlarm");
    }

    public boolean isAlarmUp() {
        return mAlarmUp;
    }

    public void setAlarmUp(boolean mAlarmUp) {
        this.mAlarmUp = mAlarmUp;
    }

    private PendingIntent createPendingIntent(Context context, String string) {
        Intent intent = new Intent(context, MoveReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void notifyMovement(long moveTimer) {
        for (MoveListener listener : mListeners) {
            listener.onMovementChanged(moveTimer);
        }
    }

    public void addMoveListener(MoveListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void removeMoveListener(MoveListener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

}
