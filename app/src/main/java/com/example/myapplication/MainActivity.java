package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    private int LOCATION_PERMISSION_CODE =1;
    private int SMS_PERMISSION_CODE = 2;
    private int CONTACTS_PERMISSION_CODE = 3;
    private int DISPLAY_OVER_APPS_PERMISSION_CODE = 4;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.btnGPS);


        TabLayout tbLy = findViewById(R.id.tabLayout);
        TabItem tbIt2 = findViewById(R.id.tabPermissions);

        ViewPager viewPgr = findViewById(R.id.viewPager);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),
                tbLy.getTabCount());
        viewPgr.setAdapter(pagerAdapter);

        //  setupTabIcons();
        tbLy.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tabSelected)
            {

                viewPgr.setCurrentItem(tabSelected.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tabSelected){

            }

            @Override
            public void onTabReselected(TabLayout.Tab tabSelected) {
                // TODO Auto-generated method stub

            }

        });


        LocationHandler locationHandler = new LocationHandler(this, "+31657792925");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationHandler.sendLocation();
            }

        });


    }
}