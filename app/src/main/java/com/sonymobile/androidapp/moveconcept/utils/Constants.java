
/*
 * Copyright (C) 2015 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */

package com.sonymobile.androidapp.moveconcept.utils;

/**
 * @file Constants.java
 * @author Gabriel Gon�alves (gabriel.goncalves@venturus.org.br)
 * @created 14/04/2015
 */
public class Constants {

    /**
     * Private Constructor: Class cnotains only static methods
     */
    private Constants() {
    }

    public static final String SHARED_PREFERENCES_NAME = "moveconcept_preferences";

    /**
     * SharedPreferences Key
     */
    public static final String KEY_START_UNMOVING = "start_unmoving";

    public static final String KEY_IDLE_LIMIT = "IDLE_LIMIT";

    /**
     * Id Notifications
     */
    public static final int NOTIFICATION_START_MOVING = 0x01;

    /**
     * Settings Constants
     */
    public static final int SCHEDULE_ALARM_REQUEST_CODE = 10000;

    /**
     * Constants
     */
    public static final long IDLE_LIMIT = 1 * 60 * 1000;

    public static final long TIME_SUGGESTION = 5 * 60 * 1000;

    public static final long TIME_ABOVE_THRESHOLD = 7 * 1000;

    /**
     * General Intents
     */
    public static final String PACKAGE_NAME = "com.sonymobile.androidapp.moveconcept";

    public static final String START_MOVE_ALARM = PACKAGE_NAME + ".action.START_MOVE_ALARM";

    /**
     * Other
     */
    public static final String VIVO_MORE_HEALTHY = "http://vc.vivo.com.br/apwms";

}
