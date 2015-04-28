/*
 * @file ${NAME}
 * @author Gabriel Gonçalves (gabriel.goncalves@venturus.org.br)
 * @created ${DATE}
 */

package com.sonymobile.androidapp.moveconcept.liveware.control.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sonyericsson.extras.liveware.motionapi.SmartMotion;
import com.sonyericsson.extras.liveware.motionapi.SmartMotionListener;
import com.sonymobile.androidapp.moveconcept.liveware.service.MoveExtensionService;
import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;
import com.sonymobile.androidapp.moveconcept.view.MainActivity;

import java.util.logging.LogManager;

/**
 * MoveMotionListener
 * Created by vntgago on 27/04/2015.
 */
public class MoveMotionListener extends SmartMotionListener {
    private static final String ELLIS_HOST_APP = "com.sonymobile.smartconnect.hostapp.ellis";

    public static final int STATE_STOPPED = 0;

    public static final int STATE_WAITING_CAPTURE = 1;
    public static final int STATE_STARTED_CAPTURE = 2;
    public static final int STATE_WAITING_RESTART = 3;

    public static final String EXTRA_STATE = "mCurrentState";
    private static int mCurrentState = STATE_WAITING_CAPTURE;
    private Context mContext;
    private Intent intentActivity;
    private float waveNewInstance1[][];
    public static final String EXTRA_WAVE_X = "waveX";
    public static final String EXTRA_WAVE_Y = "waveY";
    public static final String EXTRA_WAVE_Z = "waveZ";

    public MoveMotionListener(final Context context) {
        super(context, ELLIS_HOST_APP);
        Log.i("SmartMotion", CLASS + ": constructor");
        this.mContext = context;
    }

    @Override
    protected void onSmartMotionDetected(String s) {
        Intent intentBroadcast = new Intent();
        intentBroadcast.setAction(MoveExtensionService.EXTENSION_KEY);
        intentBroadcast.putExtra(EXTRA_STATE, mCurrentState);
        mContext.sendBroadcast(intentBroadcast);
        Log.i("SmartMotion", "SmartMotion Detected: " + s);
    }

    @Override
    protected void onSmartMotionCaptured(float[][] wave) {

        Intent intentBroadcast = new Intent();
        intentBroadcast.setAction(MoveExtensionService.EXTENSION_KEY);

        if (mCurrentState == STATE_STARTED_CAPTURE) {
            Log.i("SmartMotion", "SmartMotion Captured");
            intentBroadcast.putExtra(EXTRA_STATE, mCurrentState);
            intentBroadcast.putExtra(EXTRA_WAVE_X, wave[0]);
            intentBroadcast.putExtra(EXTRA_WAVE_Y, wave[1]);
            intentBroadcast.putExtra(EXTRA_WAVE_Z, wave[2]);
            ApplicationData.getAppContext().sendBroadcast(intentBroadcast);
        }
    }

    protected void tapAction() {
        Intent intentBroadcast = new Intent();
        intentBroadcast.setAction(MoveExtensionService.EXTENSION_KEY);
        switch (mCurrentState) {
            case STATE_STOPPED:
                Log.d("SmartMotion", CLASS + ": tapAction... STATE_STOPPED");
            case STATE_WAITING_RESTART:
                Log.d("SmartMotion", CLASS + ": tapAction... STATE_STOPPED | STATE_WAITING_RESTART");
                if (startSensor()) {
                    mCurrentState = STATE_WAITING_CAPTURE;
                    MainActivity.mListener = this;
                    Log.i("SmartMotion", "Sensor Startado");
                    intentBroadcast.putExtra(EXTRA_STATE, mCurrentState);
                    mContext.sendBroadcast(intentBroadcast);
                }
                break;
            case STATE_WAITING_CAPTURE:
                Log.i("SmartMotion", CLASS + ": tapAction... STATE_WAITING_CAPTURE");
                if (startCapturing()) {
                    mCurrentState = STATE_STARTED_CAPTURE;
                    intentBroadcast.putExtra(EXTRA_STATE, mCurrentState);
                    ApplicationData.getAppContext().sendBroadcast(intentBroadcast);
                }
                break;
        }
    }
}
