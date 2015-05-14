
package com.sonymobile.androidapp.moveconcept.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.List;
import java.util.Vector;

/**
 * @author Gabriel Gon�alves (gabriel.goncalves@venturus.org.br)
 * @file MainActivity.java
 * @created 16/04/2015
 */
public class MainActivity extends FragmentActivity {

    static final int NUM_PAGES = 2;

    MoveService mService;
    BroadcastReceiver mReceiver;

    private TextView mTimer;
    private Button mSetAlarm;
    private Button mCancelAlarm;
    boolean mBound = false;
    private Button mBtnOk;
    private Button mBtnCancel;
    private Button mBtnContinue;

    public static MoveMotionListener mListener;
    private static String mPath = "/storage/emulated/legacy/MoveConcept/";

    private PageAdapter mPagerAdapter;
    ViewPager mPager;
    boolean isOpaque = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewpager_layout);

        mTimer = (TextView) findViewById(R.id.timer);
        mSetAlarm = (Button) findViewById(R.id.btn_set_alarm);
        mCancelAlarm = (Button) findViewById(R.id.btn_cancel_alarm);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnContinue = (Button) findViewById(R.id.btn_continue);


        /** Transparent StatusBar */
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        initializeViewPager();

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
    }

    private void initializeViewPager() {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, InitialFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, ReadyFragment.class.getName()));
        mPagerAdapter = new PageAdapter(this.getSupportFragmentManager(), fragments);

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new CrossfadePageTransformer());

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
            }
        });

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplicationData.getAppContext(), MoveService.class);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    /**
     * Animate Fragments
     */
    public class CrossfadePageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();

            View backgroundView = page.findViewById(R.id.background_view);

            /** Intial components*/
            View initialImg = page.findViewById(R.id.initial_img);
            View initialTitle = page.findViewById(R.id.initial_title);
            View initialText = page.findViewById(R.id.initial_description);
            View link = page.findViewById(R.id.initial_link);

            /** Ready components*/
            View readyImg = page.findViewById(R.id.ready_img);
            View readyTitle = page.findViewById(R.id.ready_title);
            View readyText = page.findViewById(R.id.ready_description);

            if (position <= 1) {
                page.setTranslationX(pageWidth * -position);
            }

            if (position <= -1.0f || position >= 1.0f) {
            } else if (position == 0.0f) {
            } else {
                if (backgroundView != null) {
                    backgroundView.setAlpha(1.0f - Math.abs(position));
                }

                //Text both translates in/out and fades in/out
                if (initialText != null) {
                    initialText.setTranslationX(pageWidth * position);
                    initialText.setAlpha(1.0f - Math.abs(position));
                }

                if (link != null) {
                    link.setTranslationX(pageWidth * position);
                    link.setAlpha(1.0f - Math.abs(position));
                }

                if (initialImg != null) {
                    initialImg.setTranslationX((float) (pageWidth / 1.2 * position));
                }

                if (initialTitle != null) {
                    initialTitle.setTranslationX(pageWidth * position);
                    initialTitle.setTranslationX((float) (pageWidth / 1.2 * position));
                }

                if (readyImg != null) {
                    readyImg.setAlpha(1.0f - Math.abs(position));
                    readyImg.setTranslationX((float) (pageWidth / 1.2 * position));
                }
                if (readyTitle != null) {
                    readyTitle.setAlpha(1.0f - Math.abs(position));
                    readyTitle.setTranslationX((float) (pageWidth / 1.2 * position));
                }
                if (readyText != null) {
                    readyText.setAlpha(1.0f - Math.abs(position));
                    readyText.setTranslationX((float) (pageWidth / 1.2 * position));
                }

            }
        }
    }
}
