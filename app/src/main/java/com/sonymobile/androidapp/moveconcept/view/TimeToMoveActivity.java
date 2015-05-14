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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sonymobile.androidapp.moveconcept.R;
import com.sonymobile.androidapp.moveconcept.persistence.ApplicationData;
import com.sonymobile.androidapp.moveconcept.service.MoveService;
import com.sonymobile.androidapp.moveconcept.utils.Constants;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * InitialFragment
 * Created by vntgago on 12/05/2015.
 */
public class TimeToMoveActivity extends Activity {

    TextView mTextDescription;
    Button mBtnOk;
    Button mBtnDismiss;
    Context mContext = ApplicationData.getAppContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_activity_layout);

        mBtnOk = (Button)findViewById(R.id.btn_ok);
        mBtnDismiss = (Button)findViewById(R.id.btn_dismiss);

        SimpleDateFormat simpleDate = new SimpleDateFormat("mm");
        Date resultIdle = new Date(Constants.IDLE_LIMIT);
        Date resultSuggestion = new Date(Constants.TIME_SUGGESTION);

        /** Transparent StatusBar */
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mTextDescription = (TextView) findViewById(R.id.move_description);
        String msg = MessageFormat.format(mContext.getString(R.string.MV_MOVE_TEXT), simpleDate.format(resultIdle),"minutos",simpleDate.format(resultSuggestion) , "minutos" );
        mTextDescription.setText(msg);

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });

        mBtnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });
    }
}
