
/*
 * Copyright (C) 2015 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */

package com.sonymobile.androidapp.moveconcept.service;

/**
 * @author vntgago
 * @created 15/05/2015
 */

public interface MoveListener {

    public void onMovementChanged(long moveTimer);

    public void onAlarmCanceled();
}
