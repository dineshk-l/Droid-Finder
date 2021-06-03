package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class LocationHandler {
        private String number;
        private Context context;
        private Activity activity;
        private int LOCATION_PERMISSION_CODE = 2;



        private static final int LOCATION_REFRESH_TIME = 100;
        private static final int LOCATION_REFRESH_DISTANCE = 0;
        LocationManager mLocationManager;//we need location manager
        private final LocationListener myLocListener = new LocationListener() {//we add a new location listener
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.i("LocationListener", "Location Changed");
            }
            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };


        public LocationHandler(Context context, String number){
            this.number = number;
            this.context = context;
        }

        public void setNumber(String number){
            this.number = number;
        }

        public String getNumber(){
            return number;
        }



        public void sendLocation(){
            Location location = null;//set the location to null, just to be sure we get one later
            mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);//initialize location manager

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, myLocListener);//request updates

            if(mLocationManager != null){//if manager is not null then get the location

                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location != null) {//if location is not null then send the message with properties

                String messageToSend = ("Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());
                Log.i("LocationHandler","Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());
                SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);


            }


        }




}
