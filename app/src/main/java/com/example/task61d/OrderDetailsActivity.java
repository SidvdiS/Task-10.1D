package com.example.task61d;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.task61d.data.OrderDatabaseHelper;
import com.example.task61d.model.Order;
import com.example.task61d.util.OrderUtil;

public class OrderDetailsActivity extends AppCompatActivity {
    TextView receiverName, pickupDate, pickupTime, weight, length,
            width, height, goodType, vehicleType, pickupLoc, dropOffLoc;
    Button getEstimateBtn;
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

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        receiverName = findViewById(R.id.receiver_name);
        pickupDate = findViewById(R.id.order_date);
        pickupTime = findViewById(R.id.order_time);
        weight = findViewById(R.id.order_weight);
        length = findViewById(R.id.order_length);
        width = findViewById(R.id.order_width);
        height = findViewById(R.id.order_height);
        goodType = findViewById(R.id.order_type);
        vehicleType = findViewById(R.id.order_vehicle);
        toolbar = findViewById(R.id.toolbar);
        pickupLoc = findViewById(R.id.pickup_location);
        dropOffLoc = findViewById(R.id.dropoff_location);
        getEstimateBtn = findViewById(R.id.estimate_btn);
        setSupportActionBar(toolbar);

        OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(this);
        Intent i = getIntent();
        String id;
        id = i.getStringExtra(OrderUtil.ORDER_ID);
        Order order = orderDatabaseHelper.getOrderDetail(id);
        receiverName.setText(order.getReceiverName());
        pickupDate.setText(order.getPickupDate());
        pickupTime.setText(order.getPickupTime());
        weight.setText(""+order.getOrderWeight());
        length.setText(""+order.getOrderLength());
        width.setText(""+order.getOrderWidth());
        height.setText(""+order.getOrderHeight());
        pickupDate.setText(order.getPickupDate());
        pickupTime.setText(order.getPickupTime());
        goodType.setText(order.getGoodType());
        vehicleType.setText(order.getVehicleType());
        pickupLoc.setText(order.getPickupLocName());
        dropOffLoc.setText(order.getDropOffLocName());

        getEstimateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), OrderEstimateActivity.class);
                i.putExtra(OrderUtil.ORDER_ID,order.getOrderID());
                startActivity(i);
            }
        });
    }
}