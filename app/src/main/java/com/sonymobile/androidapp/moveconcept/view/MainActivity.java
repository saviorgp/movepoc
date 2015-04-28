
package com.sonymobile.androidapp.moveconcept.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sonymobile.androidapp.moveconcept.R;
import com.sonymobile.androidapp.moveconcept.liveware.control.view.MoveMotionListener;
import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;
import com.sonymobile.androidapp.moveconcept.service.MoveListener;
import com.sonymobile.androidapp.moveconcept.service.MoveService;
import com.sonymobile.androidapp.moveconcept.service.MoveService.LocalBinder;
import com.sonymobile.androidapp.moveconcept.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Gabriel Gon�alves (gabriel.goncalves@venturus.org.br)
 * @file MainActivity.java
 * @created 16/04/2015
 */
public class MainActivity extends Activity {

    MoveService mService;
    BroadcastReceiver mReceiver;

    private TextView mTimer;
    private Button mSetAlarm;
    private Button mCancelAlarm;
    boolean mBound = false;

    public static MoveMotionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mTimer = (TextView) findViewById(R.id.timer);
        mSetAlarm = (Button) findViewById(R.id.btn_set_alarm);
        mCancelAlarm = (Button) findViewById(R.id.btn_cancel_alarm);

        mSetAlarm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mBound) {
                    mService.setAlarms(getApplicationContext());
                }
            }
        });

        mCancelAlarm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mBound && mService.isAlarmUp()) {
                    mService.cancelAlarms(getApplicationContext());
                    mTimer.setText("Alarm Cancelled");
                }
            }
        });

    }

    @Override
    protected void onStart() {
        Intent intent = new Intent(ApplicationData.getAppContext(), MoveService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            Log.i("SmartMotion", "Unbinding...");
            unbindService(mConnection);
            mBound = false;
            startService(new Intent(ApplicationData.getAppContext(), MoveService.class));
        }
    }

    @Override
    protected void onPause() {
        if (mBound) {
            unregisterReceiver(mReceiver);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("SmartMotion", "ReceivingFromActivity");
                mTimer.setText("Notification!!!");
                switch (intent.getExtras().getInt(MoveMotionListener.EXTRA_STATE, 0)){
                    case MoveMotionListener.STATE_STARTED_CAPTURE:
                     Log.i("SmartMotion", ": onReceive... STATE_STARTED_CAPTURE");
                }
            }

        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.START_MOVE_ALARM);
        registerReceiver(mReceiver, filter);
    }

    public MoveListener moveListener = new MoveListener() {

        @Override
        public void onMovementChanged(long moveTimer) {
            SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm:ss");
            Date resultDate = new Date(moveTimer);
            mTimer.setText("Next Notification:\n" + simpleDate.format(resultDate));
        }
    };

    /**
     * Callbacks from service
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.addMoveListener(moveListener);
        }
    };

}
