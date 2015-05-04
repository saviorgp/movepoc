/*
 * Copyright (C) 2014 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */

package com.sonymobile.androidapp.moveconcept.utils;

import android.util.Log;

import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;

public class Logger {

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
        if (DEBUG == true) {
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
