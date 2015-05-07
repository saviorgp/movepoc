/*
 * Copyright (C) 2014 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */

package com.sonymobile.androidapp.moveconcept.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static File sFile;
    private static OutputStreamWriter myOutWriter;
    private static FileOutputStream fOut;
    static SimpleDateFormat simpleDate = new SimpleDateFormat("HH-mm-ss");
    static Date resultDate = new Date(System.currentTimeMillis());
    private static  String stamp = simpleDate.format(resultDate);
    private static String mPath = Environment.getExternalStorageDirectory().toString() + "/MoveConcept/" + stamp + ".txt";

    static {
        sFile = new File(mPath);

        if (!sFile.exists()) {
            try {
                sFile.createNewFile();
            } catch (IOException e) {
                Logger.LOGW("error: " + e.toString());
                //
            }
        }

        try {
            fOut = new FileOutputStream(sFile);
            myOutWriter = new OutputStreamWriter(fOut);
        } catch (IOException e) {
            Logger.LOGW("Deu ruim: " + e);
        }
    }

    // Log tag
    private static final String TAG = "SmartMotion";

    // Debug flag
    private static boolean DEBUG = ApplicationData.isDebuggable();

    /**
     * Enable all the logs
     */
    public static void enable() {
        DEBUG = true;
    }

    public static boolean isDebugOn() {
        return DEBUG;
    }

    /**
     * Disable all the logs
     */
    public static void disable() {
        DEBUG = false;
    }

    /**
     * Verbose logs
     *
     * @param text
     */
    public static void LOGV(String text) {
        if (DEBUG == true) {
            Log.v(TAG, text);
        }
    }

    /**
     * Verbose logs
     *
     * @param subtag
     * @param text
     */
    public static void LOGV(String subtag, String text) {
        if (DEBUG == true) {
            Log.v(TAG, subtag + " " + text);
        }
    }

    /**
     * Debug logs
     *
     * @param text
     */
    public static void LOGD(Object text) {
        if (DEBUG == true) {
            Log.d(TAG, String.valueOf(text.toString()));
        }
    }

    /**
     * Debug logs
     *
     * @param subtag
     * @param text
     */
    public static void LOGD(String subtag, String text) {
        if (DEBUG == true) {
            Log.d(TAG, subtag + " " + text);
        }
    }

    /**
     * Information logs
     *
     * @param text
     */
    public static void LOGI(String text) {
        if (DEBUG == true) {
            Log.i(TAG, text);
        }
    }

    /**
     *
     * Information logs
     *
     * @param subtag
     * @param text
     */
    public static void LOGI(String subtag, String text) {
        if (DEBUG == true) {
            Log.i(TAG, subtag + " " + text);
        }
    }

    /**
     * Warning logs
     *
     * @param text
     */
    public static void LOGW(String text) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm:ss");
        Date resultDate = new Date(System.currentTimeMillis());
        String stamp = simpleDate.format(resultDate);
        if (DEBUG == true) {
            try {
                myOutWriter.append(stamp + " ; " + text + "\r\n");
                myOutWriter.flush();
            } catch (IOException e) {
                //
            }
            Log.w(TAG, text);
        }
    }

    /**
     * Warning logs
     *
     * @param subtag
     * @param text
     */
    public static void LOGW(String subtag, String text) {
        if (DEBUG == true) {
            Log.w(TAG, subtag + " " + text);
        }
    }

    /**
     * Error logs
     *
     * @param text
     */
    public static void LOGE(String text) {
        if (DEBUG == true) {
            Log.e(TAG, text);
        }
    }

    /**
     * Error logs
     *
     * @param subtag
     * @param text
     */
    public static void LOGE(String subtag, String text) {
        if (DEBUG == true) {
            Log.e(TAG, subtag + " " + text);
        }
    }

}
