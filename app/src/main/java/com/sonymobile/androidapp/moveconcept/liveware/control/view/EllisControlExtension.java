/*
 * @file ${NAME}
 * @author Gabriel Gonçalves (gabriel.goncalves@venturus.org.br)
 * @created ${DATE}
 */

package com.sonymobile.androidapp.moveconcept.liveware.control.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonymobile.androidapp.moveconcept.liveware.service.MoveExtensionService;
import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;
import com.sonymobile.androidapp.moveconcept.service.MoveService;
import com.sonymobile.androidapp.moveconcept.utils.Constants;

/**
 * EllisControlExtension
 * Created by vntgago on 24/04/2015.
 */
public class EllisControlExtension extends ControlExtension {
    MoveService mMoveService;
    private boolean mBound = false;
    private MoveMotionListener mMoveListener;

    public EllisControlExtension(final Context context, final String hostAppPackageName) {
        super(context, hostAppPackageName);
        mMoveListener = new MoveMotionListener(context);
    }

    @Override
    public void onTap(int action, long timeStamp) {
        if (mBound) {
            switch (action) {
                case Control.TapActions.SINGLE_TAP:
                    mMoveService.setAlarms(mContext);
                    Log.i("SmartMotion", "Single Tap !");
                    break;
                case Control.TapActions.DOUBLE_TAP:
                    Log.i("SmartMotion", "Double Tap !");
                    mMoveService.cancelAlarms(mContext);
                    break;
                case Control.TapActions.TRIPLE_TAP:
                    Log.i("SmartMotion", "Triple Tap !");
                    mMoveListener.tapAction();
            }
        }
        super.onTap(action, timeStamp);
    }

    @Override
    public void onStart() {
        doBindService();
        super.onStart();
    }

    /**
     * Handle service callbacks
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MoveService.LocalBinder binder = (MoveService.LocalBinder) service;
            mMoveService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("SmartMotion", " Extension Service Disconnected");
            mBound = false;
        }
    };

    private void doBindService() {
        Intent intent = new Intent(mContext.getApplicationContext(), MoveService.class);
        mContext.bindService(intent, mServiceConnection, 0);
        Log.i("SmartMotion", "Binding Extension Service");
    }
}
