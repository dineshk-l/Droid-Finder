package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHandler {
        private String number;
        private Context context;

        private FusedLocationProviderClient fusedLocationProviderClient;

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
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location =  task.getResult();
                    if (location != null) {

                        try {
                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            String messageToSend = ("Latitude: " + addresses.get(0).getLatitude() + " Longitude: " + addresses.get(0).getLongitude());
                            System.err.println(number);
                            System.err.println("Success");
                            SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
}
