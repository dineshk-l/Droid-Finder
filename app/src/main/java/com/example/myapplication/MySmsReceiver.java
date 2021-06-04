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
        LocationHandler locationHandler = new LocationHandler(context, null);//getting the variables, most are needed for getting and reading sms messages
        InternetHandler internetHandler = new InternetHandler(context);
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        Object[] pdus = (Object[]) bundle.get(PDU_TYPE);

        updateNumbers(context);//updating trusted numbers from shared preferences everytime we receive a message
        if (pdus != null){
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);//checking the build version
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++){
                if (isVersionM){
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);//if version is right create messages
                }else {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                if (trustedNr.contains(msgs[i].getOriginatingAddress())){
                    if (msgs[i].getMessageBody().contains("turn on data")){
                        internetHandler.enableWifi();
                        internetHandler.enableMobileData();
                        internetHandler.enableGPS();
                    }
                    if (msgs[i].getMessageBody().contains("get location")){
                        locationHandler.setNumber(msgs[i].getOriginatingAddress());
                        locationHandler.sendLocation();
                    }
                    //Toast.makeText(context, "Message received from trusted number " + msgs[i].getOriginatingAddress() + ": " + msgs[i].getDisplayMessageBody(),Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "onReceive: " + strMessage);
            }
         }
    }

    private void updateNumbers(Context context) {
        trustedNr.clear();
        SharedPreferences sharedPreferences = context.getSharedPreferences(BlankFragment.SHARED_PREFS, Context.MODE_PRIVATE);
        int i = sharedPreferences.getInt(BlankFragment.COUNTER, 0);
        System.err.println(i);
        while(i > 0){
            Log.d("Number" + i, sharedPreferences.getString(BlankFragment.TEXT + i, ""));
            trustedNr.add(sharedPreferences.getString(BlankFragment.TEXT + i, ""));
            i--;
        }
    }


}