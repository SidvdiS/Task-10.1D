package com.example.task61d;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.task61d.data.TruckDatabaseHelper;
import com.example.task61d.model.Truck;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton addOrderBtn, goToMapsBtn;
    Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId()){
            case R.id.home:
                i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            break;
            case R.id.my_orders:
                i = new Intent(getApplicationContext(), MyOrdersActivity.class);
                startActivity(i);
            break;
            case R.id.logout:
                SharedPreferences sharedPreferences = getSharedPreferences("userLogin", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.truck_recycler);
        addOrderBtn = findViewById(R.id.add_order_btn);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TruckDatabaseHelper truckDatabaseHelper = new TruckDatabaseHelper(this);


        ArrayList<Truck> trucks = truckDatabaseHelper.getAllTrucks();

        RecyclerTrucksAdapter adapter = new RecyclerTrucksAdapter(this, trucks);
        recyclerView.setAdapter(adapter);

        addOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, AddDeliveryOrderActivity.class);
                startActivity(i);
            }
        });

    }
}