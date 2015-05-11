
package com.sonymobile.androidapp.moveconcept.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sonymobile.androidapp.moveconcept.R;
import com.sonymobile.androidapp.moveconcept.liveware.control.view.MoveMotionListener;
import com.sonymobile.androidapp.moveconcept.liveware.service.MoveExtensionService;
import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;
import com.sonymobile.androidapp.moveconcept.service.MoveListener;
import com.sonymobile.androidapp.moveconcept.service.MoveService;
import com.sonymobile.androidapp.moveconcept.service.MoveService.LocalBinder;
import com.sonymobile.androidapp.moveconcept.utils.Constants;
import com.sonymobile.androidapp.moveconcept.utils.Logger;

import java.text.SimpleDateFormat;
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
    private Button mRecordData;
    boolean mBound = false;

    public static MoveMotionListener mListener;
    private static String mPath = "/storage/emulated/legacy/MoveConcept/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mTimer = (TextView) findViewById(R.id.timer);
        mSetAlarm = (Button) findViewById(R.id.btn_set_alarm);
        mCancelAlarm = (Button) findViewById(R.id.btn_cancel_alarm);
        mRecordData = (Button) findViewById(R.id.btn_record_data);

        mSetAlarm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /*if (mBound) {
                    mService.setAlarms(getApplicationContext());
                }

                new SingleMediaScanner(ApplicationData.getAppContext(), mPath);*/
                Logger.LOGW("Start");
            }
        });

        mCancelAlarm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               /* if (mBound && mService.isAlarmUp()) {
                    mService.cancelAlarms(getApplicationContext());
                }*/
                Logger.LOGW("Stop");

            }
        });

        mRecordData.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /*Logger.LOGI("Start");
                mTimer.setText("Recording");*/
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
           // Logger.LOGD( "Unbinding...");
           //  mBound = false;
           //  startService(new Intent(ApplicationData.getAppContext(), MoveService.class));
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
                String action = intent.getAction();
                if (action.equals(MoveExtensionService.EXTENSION_KEY)) {
                    //Smartband usage
                    /*switch (intent.getExtras().getInt(MoveMotionListener.EXTRA_STATE, 0)) {
                        case MoveMotionListener.STATE_STARTED_CAPTURE:
                            Log.i("SmartMotion", ": onReceive... STATE_STOPPED");
                            mTimer.setText("SmartBand Move");
                            break;
                            case MoveMotionListener.STATE_FINISHED_CAPTURE:
                            wave[0] = intent.getFloatArrayExtra(MoveMotionListener.EXTRA_WAVE_X);
                            wave[0] = intent.getExtras().getFloatArray(MoveMotionListener.EXTRA_WAVE_X);
                            wave[1] = intent.getExtras().getFloatArray(MoveMotionListener.EXTRA_WAVE_Y);
                            wave[2] = intent.getExtras().getFloatArray(MoveMotionListener.EXTRA_WAVE_Z);
                            mTimer.setText("X: " + Arrays.toString(wave[0]) + "\nY: " + Arrays.toString(wave[1]) + "\nZ: " + Arrays.toString(wave[2]));
                            mTimer.setText("X: " + Arrays.toString(wave[0]));
                            break;
                    }*/
                } else if (action.equals(Constants.START_MOVE_ALARM)) {

                    Log.i("SmartMotion", "ReceivingFromActivity");
                    mTimer.setText("Notification!!!");
                }

            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.START_MOVE_ALARM);
        filter.addAction((MoveExtensionService.EXTENSION_KEY));
        registerReceiver(mReceiver, filter);
    }

    public MoveListener moveListener = new MoveListener() {

        @Override
        public void onMovementChanged(long moveTimer) {
            SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm:ss");
            Date resultDate = new Date(moveTimer);
            mTimer.setText("Next Notification:\n" + simpleDate.format(resultDate));
        }

        @Override
        public void onAlarmCanceled() {
            mTimer.setText("Alarm Cancelled");
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


    private class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {
        private MediaScannerConnection mMs;

        SingleMediaScanner(Context context, String f) {
            mMs = new MediaScannerConnection(context, this);
            mMs.connect();
        }

        @Override
        public void onMediaScannerConnected() {
            mMs.scanFile(mPath, null);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mMs.disconnect();
        }
    }

}
