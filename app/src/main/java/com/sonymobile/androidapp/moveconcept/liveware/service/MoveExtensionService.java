package com.sonymobile.androidapp.moveconcept.liveware.service;

import android.util.Log;

import com.sonyericsson.extras.liveware.extension.util.ExtensionService;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;
import com.sonymobile.androidapp.moveconcept.liveware.control.MoveExtensionRegistration;
import com.sonymobile.androidapp.moveconcept.liveware.control.view.EllisControlExtension;

/**
 * Created by vntgago on 24/04/2015.
 */
public class MoveExtensionService extends ExtensionService{
    public static final String EXTENSION_KEY = "com.sonymobile.androidapp.moveconcept.liveware.extension_key";

    public MoveExtensionService() {
        super(EXTENSION_KEY);
        Log.i("SmartMotion", "New Extension Service");
    }

    @Override
    protected RegistrationInformation getRegistrationInformation() {
        return new MoveExtensionRegistration(this);
    }

    @Override
    protected boolean keepRunningWhenConnected() {
        return false;
    }

    @Override
    public ControlExtension createControlExtension (String hostAppPackageName){
        return new EllisControlExtension(this, hostAppPackageName);
    }
}
