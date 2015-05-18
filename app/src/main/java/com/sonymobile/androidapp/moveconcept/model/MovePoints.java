/*
 * Copyright (C) 2015 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */

package com.sonymobile.androidapp.moveconcept.model;

/**
 * MovePoints
 * Created by vntgago on 07/05/2015.
 */
public class MovePoints {

    public MovePoints() {
    }

    /** G-Force measured */
    private float mGForce;

    /** Time for G-Force Measured */
    private double mGForceTime;

    public MovePoints(float mGForce, double mGForceTime) {
        this.mGForce = mGForce;
        this.mGForceTime = mGForceTime;
    }


    public float getGForce() {
        return mGForce;
    }

    public void setGForce(float mGForce) {
        this.mGForce = mGForce;
    }

    public double getGForceTime() {
        return mGForceTime;
    }

    public void setGForceTime(double mGForceTime) {
        this.mGForceTime = mGForceTime;
    }
}
