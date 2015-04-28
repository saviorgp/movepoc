package com.sonymobile.androidapp.moveconcept.liveware.control;

import android.content.ContentValues;
import android.content.Context;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.aef.registration.Registration;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.registration.DeviceInfo;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;
import com.sonyericsson.extras.liveware.extension.util.registration.TapInfo;
import com.sonymobile.androidapp.moveconcept.R;
import com.sonymobile.androidapp.moveconcept.liveware.service.MoveExtensionService;
import com.sonymobile.androidapp.moveconcept.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Every supported host application need to be registered
 */
public class MoveExtensionRegistration extends RegistrationInformation{

    final Context mContext;

    private final static List<Integer> mTaps = new ArrayList<Integer>();

    /**
     * Supported tap actions
     */
    static {
        mTaps.add(Control.TapActions.SINGLE_TAP);
        mTaps.add(Control.TapActions.DOUBLE_TAP);
    }

    /**
     * @param context
     */
    public MoveExtensionRegistration(Context context) {
        if (context == null){
            throw new IllegalArgumentException("context == null");
        }
        mContext = context;
    }

    @Override
    public int getRequiredNotificationApiVersion() {
        return RegistrationInformation.API_NOT_REQUIRED;
    }

    @Override
    public int getRequiredWidgetApiVersion() {
        return RegistrationInformation.API_NOT_REQUIRED;
    }

    @Override
    public int getRequiredControlApiVersion() {
        //Tap support was added in Control API level 3
        return 3;
    }

    @Override
    public int getRequiredSensorApiVersion() {
        return RegistrationInformation.API_NOT_REQUIRED;
    }

    /**
     * Get the extension registration information
     * @return Registration configuration
     */
    @Override
    public ContentValues getExtensionRegistrationConfiguration() {
        String iconHostapp = ExtensionUtils.getUriString(mContext, R.drawable.ic_launcher);
        String iconExtension = ExtensionUtils.getUriString(mContext, R.drawable.ic_launcher);

        ContentValues values = new ContentValues();
        values.put(Registration.ExtensionColumns.CONFIGURATION_TEXT, mContext.getString(R.string.MV_APP_NAME));
        values.put(Registration.ExtensionColumns.CONFIGURATION_ACTIVITY, MainActivity.class.getName());
        values.put(Registration.ExtensionColumns.NAME, mContext.getString(R.string.MV_APP_NAME));
        values.put(Registration.ExtensionColumns.EXTENSION_KEY, MoveExtensionService.EXTENSION_KEY);
        values.put(Registration.ExtensionColumns.NOTIFICATION_API_VERSION, getRequiredNotificationApiVersion());
        values.put(Registration.ExtensionColumns.PACKAGE_NAME, mContext.getPackageName());

        return values;
    }

    /**
     * Check if host application support any of declared tap actions
     * @param deviceInfo
     * @return
     */
    @Override
    public boolean isControlDeviceSupported(DeviceInfo deviceInfo) {

        for (TapInfo tapInfo : deviceInfo.getTaps()){
            if(mTaps.contains(tapInfo.getTapAction())){
                return true;
            }
        }
        return false;
    }
}
