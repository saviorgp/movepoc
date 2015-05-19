
/*
 * Copyright (C) 2015 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */

package com.sonymobile.androidapp.moveconcept.view;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.sonymobile.androidapp.moveconcept.R;
import com.sonymobile.androidapp.moveconcept.liveware.control.view.MoveMotionListener;
import com.sonymobile.androidapp.moveconcept.liveware.service.MoveExtensionService;
import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;
import com.sonymobile.androidapp.moveconcept.service.MoveListener;
import com.sonymobile.androidapp.moveconcept.service.MoveService;
import com.sonymobile.androidapp.moveconcept.service.MoveService.LocalBinder;
import com.sonymobile.androidapp.moveconcept.utils.Constants;

import java.util.List;
import java.util.Vector;

/**
 * @author Gabriel Goncalves (gabriel.goncalves@sonymobile.com)
 * @file MainActivity.java
 * @created 16/04/2015
 */
public class MainActivity extends FragmentActivity {

    static final int NUM_PAGES = 2;

    MoveService mService;
    BroadcastReceiver mReceiver;

    private Button mSetAlarm;
    private Button mCancelAlarm;
    boolean mBound = false;
    private Button mBtnOk;
    private Button mBtnCancel;
    private Button mBtnContinue;

    public static MoveMotionListener mListener;

    private PageAdapter mPagerAdapter;
    ViewPager mPager;
    boolean isOpaque = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewpager_layout);

        mSetAlarm = (Button) findViewById(R.id.btn_set_alarm);
        mCancelAlarm = (Button) findViewById(R.id.btn_cancel_alarm);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnContinue = (Button) findViewById(R.id.btn_continue);

        /** Transparent StatusBar */
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        initializeViewPager();
        //mService.setAlarms(getApplicationContext());

        mSetAlarm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /*if (mBound) {
                    mService.setAlarms(getApplicationContext());
                }*/
            }
        });

        mCancelAlarm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               /* if (mBound && mService.isAlarmUp()) {
                    mService.cancelAlarms(getApplicationContext());
                }*/
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
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.addMoveListener(moveListener);
            mService.setAlarms(ApplicationData.getAppContext());
        }
    };

    private void initializeViewPager() {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, InitialFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, ReadyFragment.class.getName()));
        mPagerAdapter = new PageAdapter(this.getSupportFragmentManager(), fragments);

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new CrossfadePage());

        final Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == NUM_PAGES - 2 && positionOffset > 0) {
                    if (isOpaque) {
                        mPager.setBackgroundColor(Color.TRANSPARENT);
                        isOpaque = false;
                    }
                } else {
                    if (!isOpaque) {
                        mPager.setBackgroundColor(getResources().getColor(R.color.blue_bgcolor));
                        isOpaque = true;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBtnContinue.setVisibility(View.VISIBLE);
                        mBtnOk.setVisibility(View.GONE);
                        mBtnCancel.setVisibility(View.GONE);
                        break;

                    case 1:
                        mBtnContinue.setVisibility(View.GONE);
                        mBtnOk.setVisibility(View.VISIBLE);
                        mBtnCancel.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Do nothing
            }
        });

        mBtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(1);
                v.setVisibility(View.GONE);
                mBtnOk.setVisibility(View.VISIBLE);
                mBtnCancel.setVisibility(View.VISIBLE);
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(0);
                v.setVisibility(View.VISIBLE);
                mBtnOk.setVisibility(View.GONE);
                mBtnCancel.setVisibility(View.GONE);
                startActivity(startMain);
            }
        });

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplicationData.getAppContext(), MoveService.class);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                startActivity(startMain);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            //TODO
            //Service
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
            //For UI update
        }

        @Override
        public void onAlarmCanceled() {
            //For UI update
        }
    };
}
