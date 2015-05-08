/*
 * @file ${NAME}
 * @author Gabriel Gonçalves (gabriel.goncalves@venturus.org.br)
 * @created ${DATE}
 */

package com.sonymobile.androidapp.moveconcept.move;

import com.sonymobile.androidapp.moveconcept.utils.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * MoveMeters
 * Created by vntgago on 07/05/2015.
 */
public class MoveMeter {

    Timer timer;


    public MoveMeter(int seconds) {
        timer = new Timer();
        timer.schedule(new MoveMeterTask(), seconds*1000);
    }

    class MoveMeterTask extends TimerTask{

        @Override
        public void run() {
            Logger.LOGI("Moving after 7 seconds");
        }
    }
}
