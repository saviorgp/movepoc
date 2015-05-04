
package com.sonymobile.androidapp.moveconcept.persistence;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @file ApplicationData.java
 * @author Gabriel Gon�alves (gabriel.goncalves@venturus.org.br)
 * @created 14/04/2015
 */
public class ApplicationData extends Application {

    private static SharedPreferencesHelper sSharedPreferencesHelper;

    /** Global application context */
    private static Context sApplicationContext = null;

    public static boolean isDebuggable() {
        return sDebuggable;
    }

    public static void setDebuggable(boolean sDebuggable) {
        ApplicationData.sDebuggable = sDebuggable;
    }

    public static boolean sDebuggable;

    @Override
    public void onCreate() {
        super.onCreate();
        //
        setDebuggable(true);
        setSharedPreferences(new SharedPreferencesHelper(getApplicationContext()));
        setApplicationContext(getApplicationContext());
    }

    /**
     * Load users preference
     */
    private void loadPreferences() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... param) {
                sSharedPreferencesHelper.loadPreferences();
                return null;
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public static SharedPreferencesHelper getSharedPreferences() {
        return sSharedPreferencesHelper;
    }

    public static void setSharedPreferences(SharedPreferencesHelper preferences) {
        sSharedPreferencesHelper = preferences;
    }

    public static Context getAppContext() {
        return sApplicationContext;
    }

    public static void setApplicationContext(Context value) {
        ApplicationData.sApplicationContext = value;
    }

}
