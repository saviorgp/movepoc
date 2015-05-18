/*
 * Copyright (C) 2015 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */
package com.sonymobile.androidapp.moveconcept.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sonymobile.androidapp.moveconcept.R;
import com.sonymobile.androidapp.moveconcept.utils.Constants;

/**
 * InitialFragment
 * Created by vntgago on 12/05/2015.
 */
public class InitialFragment extends Fragment {

    TextView mLink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = (RelativeLayout) inflater.inflate(R.layout.initial_fragment_layout, container, false);
        mLink = (TextView) view.findViewById(R.id.initial_link);

        if (mLink != null) {
            mLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(Constants.VIVO_MORE_HEALTHY));
                    startActivity(browserIntent);
                }
            });
        }

        return view;
    }

}
