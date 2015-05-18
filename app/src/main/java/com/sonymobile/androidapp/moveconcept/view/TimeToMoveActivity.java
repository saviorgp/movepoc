/*
 * Copyright (C) 2015 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */

package com.sonymobile.androidapp.moveconcept.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sonymobile.androidapp.moveconcept.R;
import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;
import com.sonymobile.androidapp.moveconcept.service.MoveService;
import com.sonymobile.androidapp.moveconcept.utils.Constants;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * InitialFragment
 * Created by vntgago on 12/05/2015.
 */
public class TimeToMoveActivity extends Activity {

    TextView mTextDescription;
    Button mBtnOk;
    Button mBtnDismiss;
    Context mContext = ApplicationData.getAppContext();
    MoveService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_activity_layout);

        mBtnOk = (Button)findViewById(R.id.btn_ok);
        mBtnDismiss = (Button)findViewById(R.id.btn_dismiss);

        SimpleDateFormat simpleDate = new SimpleDateFormat("mm");
        Date resultIdle = new Date(Constants.IDLE_LIMIT);
        Date resultSuggestion = new Date(Constants.TIME_SUGGESTION);

        changeStatusBar();

        mTextDescription = (TextView) findViewById(R.id.move_description);
        String msg = MessageFormat.format(mContext.getString(R.string.MV_MOVE_TEXT), simpleDate.format(resultIdle),mContext.getString(R.string.MV_TIME_MINUTES),simpleDate.format(resultSuggestion) , mContext.getString(R.string.MV_TIME_MINUTES) );
        mTextDescription.setText(msg);

        Intent intent = new Intent(ApplicationData.getAppContext(), MoveService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.setAlarms(ApplicationData.getAppContext());

                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });

        mBtnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });
    }

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
            MoveService.LocalBinder binder = (MoveService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }
    };

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void changeStatusBar(){
        /** Transparent StatusBar */
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

}
