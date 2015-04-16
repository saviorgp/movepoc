
package com.sonymobile.androidapp.moveconcept.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.sonymobile.androidapp.moveconcept.R;
import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;
import com.sonymobile.androidapp.moveconcept.persistence.SharedPreferencesHelper;
import com.sonymobile.androidapp.moveconcept.service.CompareIdleTimeService;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;

    private Sensor mSensorAccelerometer;

    private SharedPreferencesHelper prefs;

    private long lastUpdate = 0;

    private float lastx, lasty, lastz;

    private static final int SHAKE_THRESHOLD = 400;

    private long mLastShake = 0;

    private TextView mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
        setContentView(R.layout.activity_main);

        mTimer = (TextView)findViewById(R.id.timer);

        startService(new Intent(ApplicationData.getAppContext(), CompareIdleTimeService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

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
                    mTimer.setText("0");
                }

                lastx = x;
                lasty = y;
                lastz = z;
            }
        }
    }
}
