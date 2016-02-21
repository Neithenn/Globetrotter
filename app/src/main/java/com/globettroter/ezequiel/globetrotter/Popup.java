package com.globettroter.ezequiel.globetrotter;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Ezequiel on 02/01/2016.
 * NEW SMALL ACTIVITY WITH A MESSAGE
 */
public class Popup extends Mensaje {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_points);
        TextView tx =(TextView) findViewById(R.id.msgText);
        Button bt_ok = (Button) findViewById(R.id.ok_button);
        String msg = getIntent().getStringExtra("msg");

        Log.i("MSG", " " + msg);

        tx.setText(msg);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.2));

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();


            }
        });

    }
}
