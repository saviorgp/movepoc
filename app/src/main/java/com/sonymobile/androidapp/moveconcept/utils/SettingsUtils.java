/*
 * Copyright (C) 2015 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */

package com.sonymobile.androidapp.moveconcept.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Gabriel M. Goncalves (gabriel.goncalves@sonymobile.com)
 * @file SettingsUtils
 * @created 15/05/2015
 */
public class SettingsUtils {

    public static String currentMillisToDate(long time, String format){

        SimpleDateFormat simpleDate = new SimpleDateFormat(format);
        Date resultAlarm = new Date(time);

        return simpleDate.format(resultAlarm);
    };
}
