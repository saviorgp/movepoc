/*
 * Copyright (C) 2015 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */

package com.sonymobile.androidapp.moveconcept.view;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.sonymobile.androidapp.moveconcept.R;

/**
 * Animate Fragments
 *
 * @author Gabriel M. Goncalves (gabriel.goncalves@sonymobile.com)
 * @file CrossfadePageTransformer
 * @created 19/05/2015
 */
public class CrossfadePage implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();

        View backgroundView = page.findViewById(R.id.background_view);

        /** Intial components*/
        View initialImg = page.findViewById(R.id.initial_img);
        View initialTitle = page.findViewById(R.id.initial_title);
        View initialText = page.findViewById(R.id.initial_description);
        View link = page.findViewById(R.id.initial_link);

        /** Ready components*/
        View readyImg = page.findViewById(R.id.ready_img);
        View readyTitle = page.findViewById(R.id.ready_title);
        View readyText = page.findViewById(R.id.ready_description);

        if (position <= 1) {
            page.setTranslationX(pageWidth * -position);
        }

        if (position <= -1.0f || position >= 1.0f) {
        } else if (position == 0.0f) {
        } else {
            if (backgroundView != null) {
                backgroundView.setAlpha(1.0f - Math.abs(position));
            }

            //Text both translates in/out and fades in/out
            if (initialText != null) {
                initialText.setTranslationX(pageWidth * position);
                initialText.setAlpha(1.0f - Math.abs(position));
            }

            if (link != null) {
                link.setTranslationX(pageWidth * position);
                link.setAlpha(1.0f - Math.abs(position));
            }

            if (initialImg != null) {
                initialImg.setTranslationX((float) (pageWidth / 1.2 * position));
            }

            if (initialTitle != null) {
                initialTitle.setTranslationX(pageWidth * position);
                initialTitle.setTranslationX((float) (pageWidth / 1.2 * position));
            }

            if (readyImg != null) {
                readyImg.setAlpha(1.0f - Math.abs(position));
                readyImg.setTranslationX((float) (pageWidth / 1.2 * position));
            }
            if (readyTitle != null) {
                readyTitle.setAlpha(1.0f - Math.abs(position));
                readyTitle.setTranslationX((float) (pageWidth / 1.2 * position));
            }
            if (readyText != null) {
                readyText.setAlpha(1.0f - Math.abs(position));
                readyText.setTranslationX((float) (pageWidth / 1.2 * position));
            }

        }
    }
}