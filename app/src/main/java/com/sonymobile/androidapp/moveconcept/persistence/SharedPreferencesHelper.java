
package com.sonymobile.androidapp.moveconcept.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.sonymobile.androidapp.moveconcept.utils.Constants;

import java.util.Set;

/**
 * @file SharedPreferencesHelper.java
 * @author Gabriel Goncalves (gabriel.goncalves@venturus.org.br)
 * @created 14/04/2015
 */

public class SharedPreferencesHelper {

    /**
     * Instance
     */
    private SharedPreferences mSharedPreferences;

    /**
     * 
     */
    private long mStartUnmoving;

    private boolean mStartUnmovingStatus = true;

    public SharedPreferencesHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
    }

    /**
     * Load Users Preferences
     */
    public void loadPreferences() {

        mStartUnmoving = mSharedPreferences.getLong(Constants.KEY_START_UNMOVING, 30);
    }

    @SuppressWarnings("unchecked")
    public void editPreferences(final String key, final Object value) {
        SharedPreferences.Editor sharedEditor = mSharedPreferences.edit();
        if (value instanceof Float) {
            sharedEditor.putFloat(key, Float.parseFloat(value.toString()));
        } else if (value instanceof String) {
            sharedEditor.putString(key, String.valueOf(value));
        } else if (value instanceof Long) {
            sharedEditor.putLong(key, Long.parseLong(value.toString()));
        } else if (value instanceof Set) {
            sharedEditor.putStringSet(key, (Set<String>)value);
        } else {
            // If value is null then remove it from sharedpref
            sharedEditor.remove(key);
        }
        sharedEditor.apply();
    }

    public Long getStartUnmoving() {
        return mStartUnmoving;
    }

    public void setStartUnmoving(Long value) {
        mStartUnmoving = value;
        editPreferences(Constants.KEY_START_UNMOVING, value);
    }

    public boolean isStartUnmovingStatus() {
        return mStartUnmovingStatus;
    }

    public void setStartUnmovingStatus(boolean value) {
        mStartUnmovingStatus = value;
    }

}
