package com.example.myapplication;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import java.util.LinkedList;
import java.util.List;

public class MySmsReceiver extends BroadcastReceiver {
    private static final String TAG = MySmsReceiver.class.getSimpleName();
    public static final String PDU_TYPE = "pdus";
    List<String> trustedNr = new LinkedList<String>();

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        LocationHandler locationHandler = new LocationHandler(context, null);
        InternetHandler internetHandler = new InternetHandler(context);
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        Object[] pdus = (Object[]) bundle.get(PDU_TYPE);
        updateNumbers(context);
        if (pdus != null){
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++){
                if (isVersionM){
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                }else {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                strMessage += "SMS from" + msgs[i].getOriginatingAddress();
                strMessage += " :" +  msgs[i].getMessageBody() + "\n";

                if (trustedNr.contains(msgs[i].getOriginatingAddress())){
                    if (msgs[i].getMessageBody().contains("turn on data")){
                        System.err.println("data");
                        internetHandler.enableWifi();
                        internetHandler.enableMobileData();
                        internetHandler.enableGPS();
                    }
                    if (msgs[i].getMessageBody().contains("get location")){

                        locationHandler.setNumber(msgs[i].getOriginatingAddress());
                        locationHandler.sendLocation();
                    }
                }
                Log.d(TAG, "onReceive: " + strMessage);
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateNumbers(Context context) {
        trustedNr.clear();
        SharedPreferences sharedPreferences = context.getSharedPreferences(BlankFragment.SHARED_PREFS, Context.MODE_PRIVATE);
        int i = sharedPreferences.getInt(BlankFragment.COUNTER, 0);
        System.err.println(i);
        while(i > 0){
            Log.d("Number" + i, sharedPreferences.getString(BlankFragment.TEXT + i, "a"));
            trustedNr.add(sharedPreferences.getString(BlankFragment.TEXT + i, ""));
            i--;
        }
    }


}