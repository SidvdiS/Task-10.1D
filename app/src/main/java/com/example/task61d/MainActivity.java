package com.example.task61d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.task61d.data.OrderDatabaseHelper;
import com.example.task61d.data.TruckDatabaseHelper;
import com.example.task61d.model.Truck;
import com.example.task61d.util.TruckUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("userLogin", MODE_PRIVATE);
        Boolean check = sharedPreferences.getBoolean("isLoggedIn", false);


        TruckDatabaseHelper db = new TruckDatabaseHelper(MainActivity.this);
        if(db.trucksAvail()==0){
            db.insertTruck(new Truck("John Morrison","047281912", TruckUtil.TYPE_BOX,TruckUtil.INFO_BOX));
            db.insertTruck(new Truck("Mark Goldbridge","04732112", TruckUtil.TYPE_FLATBED,TruckUtil.INFO_FLATBED));
            db.insertTruck(new Truck("Matthew","11181912", TruckUtil.TYPE_LOG,TruckUtil.INFO_LOG));
            db.insertTruck(new Truck("Josh","892321912", TruckUtil.TYPE_REFRIGERATED,TruckUtil.INFO_REFRIGERATED));
            db.insertTruck(new Truck("Ali ","190281912", TruckUtil.TYPE_TANKER,TruckUtil.INFO_TANKER));
            db.insertTruck(new Truck("Ram","732231912", TruckUtil.TYPE_VAN,TruckUtil.INFO_VAN));
            db.insertTruck(new Truck("Pedro","047281912", TruckUtil.TYPE_MINI,TruckUtil.INFO_MINI));
            db.insertTruck(new Truck("Rick","091281912", TruckUtil.TYPE_TOW,TruckUtil.INFO_TOW));
        }




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if(check){
                    i= new Intent(MainActivity.this, HomeActivity.class);
                }
                else{
                    i= new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(i);
                finish();
            }
        },1000);
    }
}