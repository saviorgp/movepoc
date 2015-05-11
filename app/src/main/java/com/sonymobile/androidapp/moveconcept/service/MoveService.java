
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
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.sonymobile.androidapp.moveconcept.model.MovePoints;
import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;
import com.sonymobile.androidapp.moveconcept.persistence.SharedPreferencesHelper;
import com.sonymobile.androidapp.moveconcept.receiver.MoveReceiver;
import com.sonymobile.androidapp.moveconcept.utils.Constants;
import com.sonymobile.androidapp.moveconcept.utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MoveService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensorAccelerometer;
    private final SensorEventListener mSensorListener = this;
    private SharedPreferencesHelper prefs = ApplicationData.getSharedPreferences();
    private long mLastShakeTime = 0;
    private static final int TIMEOUT_INTERVAL = 100;
    private static final int TIMEOUT_STEP_FACTOR = 2;

    /** Normal value is 1g (Use 0.8 threshold for future calibrate) */
    private static final double GFORCE_THRESHOLD = 0.8;
    private boolean mAlarmUp = false;
    final public static String ONE_TIME = "onetime";
    private static Set<MoveListener> mListeners = new HashSet<>();
    private int stepCounter = 0;
    private long shakeTimeDetected = 0;
    private long stepTimeDetected = 0;
    private float mMedia = 0;
    private long teste = 0;

    final Handler mHandler = new Handler();
    MovePoints mMovePoints;
    private List<MovePoints> mListPoints = new ArrayList<>();

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
        registerListener();
        mRunnable.run();

    }

    /**
     * Runnable to control getting points frame
     */
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            scheduleNextWindow();
        }
    };

    /**
     * Schedule Next Notification
     */
    private synchronized void scheduleNextWindow() {
        float mSum = 0;
        float mMedia = 0;

        if (!mListPoints.isEmpty()){
            compareAndSort();
            for (MovePoints values : mListPoints) {
                mSum += values.getGForce();
                Logger.LOGI("values: " + values.getGForce());
            }
            Logger.LOGI("Soma " + mSum);

            mMedia = mSum / mListPoints.size();
            Logger.LOGI(">>>>>>>>Desvio Padrao: " + calculateStandardDeviation(mMedia));
            if (mMedia > 1.15f)
                Logger.LOGW("------------------------Moving------------------------------ ");
            Logger.LOGW("------------------------teste: " + teste + " ----------------------------------media: " + mMedia);

            teste += 1;
        }
        mListPoints.clear();
        mHandler.postDelayed(mRunnable, 7000);
    }

    /**
     * Compare each attribute of points, and use to sort
     */
    private void compareAndSort(){
        Comparator compareToSort = new Comparator<MovePoints>() {
            @Override
            public int compare(MovePoints lhs, MovePoints rhs) {
                return new Float(lhs.getGForce()).compareTo(new Float(rhs.getGForce()));
            }
        };
        Collections.sort(mListPoints, compareToSort);
    }


    private double calculateStandardDeviation(float mMedia){
        float stdDev = 0;

        for (MovePoints values : mListPoints) {
            stdDev += ((values.getGForce() - mMedia) * (values.getGForce() - mMedia));
            Logger.LOGI("values: " + values.getGForce());
        }

        return Math.sqrt(stdDev/mMedia);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // setAlarms(this);
        Log.i("SmartMotion", "INSTANCE");
        return START_STICKY;
    }

    public void registerListener() {
        mSensorManager.registerListener(mSensorListener, mSensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            float gravityForce = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

            if (gravityForce > GFORCE_THRESHOLD && (System.currentTimeMillis() - mLastShakeTime) > (TIMEOUT_INTERVAL / 2)) {
                if (System.currentTimeMillis() - shakeTimeDetected > TIMEOUT_INTERVAL && System.currentTimeMillis() - stepTimeDetected > TIMEOUT_INTERVAL * TIMEOUT_STEP_FACTOR) {
                    mMovePoints = new MovePoints(gravityForce, System.currentTimeMillis());
                    mListPoints.add(mMovePoints);
                    stepTimeDetected = System.currentTimeMillis();
                    mMedia = (mMedia + gravityForce);
                    stepCounter += 1;
                    Logger.LOGI("HandshakeGlobalService.onSensorEvent()... stepCounter = " + stepCounter);
                    Logger.LOGI("Seconds" + TimeUnit.MILLISECONDS.toSeconds(stepTimeDetected));
                    Logger.LOGI(Float.toString(gravityForce));
                }

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
