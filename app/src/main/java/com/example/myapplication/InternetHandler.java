package com.example.myapplication;

import android.content.Context;
import android.net.wifi.WifiManager;

public class InternetHandler {
    private WifiManager wifiManager;
    private Context context;
    public InternetHandler(Context c){
        context = c;

    }

    public void wifiOn(){
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
    }
}
