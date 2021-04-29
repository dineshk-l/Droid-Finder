package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textview1 = (TextView) findViewById(R.id.txtHello);
        Button button1 = (Button) findViewById(R.id.btnGPS);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageToSend = "this is a message";
                String number = "+31657792925";

                SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null,null);
            }
        });
    }
}