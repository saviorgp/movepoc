
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
import com.sonymobile.androidapp.moveconcept.utils.Logger;

import java.util.HashSet;
import java.util.Set;

public class MoveService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;

    private Sensor mSensorAccelerometer;

    private SharedPreferencesHelper prefs = ApplicationData.getSharedPreferences();

    private long mLastShakeTime = 0;

    private float lastx, lasty, lastz;

    private static final int SHAKE_THRESHOLD = 400;

    private static final int TIMEOUT_INTERVAL = 100;
    private static final int TIMEOUT_STEP_FACTOR = 2;

    private static final int GFORCE_THRESHOLD = 2;

    private long mLastShake = 0;

    private boolean mAlarmUp = false;

    final public static String ONE_TIME = "onetime";

    private static Set<MoveListener> mListeners = new HashSet<MoveListener>();

    private int handshakeCounter = 0;
    private int stepCounter = 0;
    private long shakeTimeDetected = 0;
    private long stepTimeDetected = 0;

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
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
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

            float gravityForce = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

            if (gravityForce > GFORCE_THRESHOLD && (System.currentTimeMillis() - mLastShakeTime) > (TIMEOUT_INTERVAL /2 )) {
                if (System.currentTimeMillis() - shakeTimeDetected > TIMEOUT_INTERVAL && System.currentTimeMillis() - stepTimeDetected > TIMEOUT_INTERVAL * TIMEOUT_STEP_FACTOR) {
                    if ((y > 15 || y < -15) && (z > -2)) {
                        shakeTimeDetected = System.currentTimeMillis();
                        handshakeCounter += 1;
                        Logger.LOGI("HandshakeGlobalService.onSensorEvent()... handshakeCounter = " + handshakeCounter);

                    } else {
                        stepTimeDetected = System.currentTimeMillis();
                        stepCounter += 1;
                        Logger.LOGI("HandshakeGlobalService.onSensorEvent()... stepCounter = " + stepCounter);
                    }
                }

                /*float speed = Math.abs(x + y + z - lastx - lasty - lastz) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    if (mLastShake == 0) {
                        mLastShake = System.currentTimeMillis();
                    }
                    prefs.setStartUnmoving(mLastShake);
                    Log.i("SmartMotion", "Set: "
                            + ApplicationData.getSharedPreferences().getStartUnmoving());
                    mLastShake = System.currentTimeMillis();
                    setAlarms(ApplicationData.getAppContext());
                }
                lastx = x;
                lasty = y;
                lastz = z;*/
            }
            mLastShakeTime = System.currentTimeMillis();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setAlarms(Context context) {
        AlarmManager moveAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
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
        AlarmManager moveAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Constants.START_MOVE_ALARM);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                Constants.SCHEDULE_ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notifyAlarmCanceled();
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

    public void notifyAlarmCanceled() {
        for (MoveListener listener : mListeners) {
            listener.onAlarmCanceled();
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
