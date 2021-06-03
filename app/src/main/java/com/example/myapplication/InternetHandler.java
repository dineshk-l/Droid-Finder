package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class InternetHandler {
    private static final int AUTO_START_ENABLE_REQUEST = 1;
    private WifiManager wifiManager;
    private Context context;
    public InternetHandler(Context c){
        context = c;

    }

    public void enableMobileData(){

        try {
            String[] cmds = {"svc data enable"};//command for super-user
            Process p = Runtime.getRuntime().exec("su");//we are using super-user process
            DataOutputStream os = new DataOutputStream(p.getOutputStream());//we need to stream the info to the shell of the phone
            for (String tmpCmd : cmds) {
                os.writeBytes(tmpCmd + "\n");//writing all the commands to shell, in this case we have one
            }
            os.writeBytes("exit\n");//exiting
            os.flush();//clearing data
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void enableWifi(){
        try {
            String[] cmds = {"svc wifi enable"};
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

    public void enableGPS(){
        String[] cmds = {"pm grant com.your_app_packagename android.permission.WRITE_SECURE_SETTINGS","settings put secure location_mode 3"};
        //we have different command for enabling gps, but the other part of this method is the same.
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            for (String tmpCmd : cmds) {
                os.writeBytes(tmpCmd + "\n");
            }
            os.writeBytes("exit\n");
            os.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public void activateLocation(boolean highAccuracy) {

        int currentMode =0;
        Integer newMode = null;

        if (highAccuracy) {
            if (currentMode != Settings.Secure.LOCATION_MODE_HIGH_ACCURACY) {
                newMode = Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;
            }
        } else {
            if (currentMode == Settings.Secure.LOCATION_MODE_OFF) {
                newMode = Settings.Secure.LOCATION_MODE_BATTERY_SAVING;
            }
        }

        if (newMode == null) {
            return;
        }

    }
    public void turnGpsOn (Context context) {
        String beforeEnable = Settings.Secure.getString (context.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        String newSet = String.format ("%s,%s",
                beforeEnable,
                LocationManager.GPS_PROVIDER);
        try {
            Settings.Secure.putString (context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED,
                    newSet);
        } catch(Exception e) {}
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
