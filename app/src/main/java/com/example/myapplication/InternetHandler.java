package com.example.myapplication;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.io.DataOutputStream;

public class InternetHandler {
    private WifiManager wifiManager;
    private Context context;
    public InternetHandler(Context c){
        context = c;

    }

    public void enableMobileData(){

        try {
            String[] cmds = {"svc data enable"};
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            for (String tmpCmd : cmds) {
                os.writeBytes(tmpCmd + "\n");
            }
            os.writeBytes("exit\n");
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void wifiOn(){
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
    }
}
