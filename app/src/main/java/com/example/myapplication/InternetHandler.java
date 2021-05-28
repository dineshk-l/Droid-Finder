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
    public void openMiuiAutoStartPermissionActivity(Context context, Activity activity) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        String ROM = getMiUiVersionProperty();
        if (TextUtils.equals(ROM, "V5")) {
            PackageInfo pInfo = null;
            try {
                pInfo = context.getPackageManager().getPackageInfo(                                                      context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            intent.setClassName("com.android.settings",    "com.miui.securitycenter. permission.AppPermissionsEditor");
            intent.putExtra("extra_package_uid", pInfo.applicationInfo.uid);
        } else if (TextUtils.equals(ROM, "V6") || TextUtils.equals(ROM, "V7") || TextUtils.equals(ROM, "V8")) {
            intent.setClassName( "com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
            intent.putExtra( "extra_pkgname", context.getPackageName());
        } else {
            intent.setClassName( "com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
            intent.putExtra( "extra_pkgname", context.getPackageName());
        }
        if (isIntentAvailable(context, intent) && (context instanceof Activity)) {
            ((Activity) context).startActivityForResult(intent, AUTO_START_ENABLE_REQUEST);
        }
    }

    public String getMiUiVersionProperty() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop ro.miui.ui.version.name").getInputStream()), 1024);
            String line = reader.readLine();
            reader.close();
            return line;
        } catch (IOException e) {}
        return null;
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
