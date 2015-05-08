/*
 * @file ${NAME}
 * @author Gabriel Gonçalves (gabriel.goncalves@venturus.org.br)
 * @created ${DATE}
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
    private long mGForceTime;

    public MovePoints(float mGForce, long mGForceTime) {
        this.mGForce = mGForce;
        this.mGForceTime = mGForceTime;
    }


    public float getGForce() {
        return mGForce;
    }

    public void setGForce(float mGForce) {
        this.mGForce = mGForce;
    }

    public long getGForceTime() {
        return mGForceTime;
    }

    public void setGForceTime(long mGForceTime) {
        this.mGForceTime = mGForceTime;
    }
}
