package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.material.tabs.TabLayout;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> myNumbers = new LinkedList<String>();
    public static final String KEY = "com.example.myapplication.EXTRA_NUMBER";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tbLy = findViewById(R.id.tabLayout);

        ViewPager viewPgr = findViewById(R.id.viewPager);

        PagerAdapter pagerAdapter;
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),
                tbLy.getTabCount());
        viewPgr.setAdapter(pagerAdapter);


        //  setupTabIcons();
        tbLy.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tabSelected) {

                viewPgr.setCurrentItem(tabSelected.getPosition());
            }



            @Override
            public void onTabUnselected(TabLayout.Tab tabSelected) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tabSelected) {
                // TODO Auto-generated method stub

            }

        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        return true;
    }

    public List<String> getList(){
        return myNumbers;
    }


}