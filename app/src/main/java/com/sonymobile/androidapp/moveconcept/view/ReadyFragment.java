/*
 * @file ${NAME}
 * @author Gabriel Gonçalves (gabriel.goncalves@venturus.org.br)
 * @created ${DATE}
 */

/*
 * @file ${NAME}
 * @author Gabriel Gonçalves (gabriel.goncalves@venturus.org.br)
 * @created ${DATE}
 */

package com.sonymobile.androidapp.moveconcept.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sonymobile.androidapp.moveconcept.R;

/**
 * InitialFragment
 * Created by vntgago on 12/05/2015.
 */
public class ReadyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        return (RelativeLayout) inflater.inflate(R.layout.ready_fragment_layout, container, false);
    }
}
