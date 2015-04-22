
package com.sonymobile.androidapp.moveconcept.utils;

/**
 * @file Constants.java
 * @author Gabriel Gonçalves (gabriel.goncalves@venturus.org.br)
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
     * Constants
     */
    public static final long IDLE_LIMIT = 5 * 1000;

}
