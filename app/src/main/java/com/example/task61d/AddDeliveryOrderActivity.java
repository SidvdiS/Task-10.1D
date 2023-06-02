package com.example.task61d;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.task61d.data.OrderDatabaseHelper;
import com.example.task61d.model.Order;
import com.example.task61d.util.TruckUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AddDeliveryOrderActivity extends AppCompatActivity {
    EditText receiverName, goodType, weight, width, length, height;
    DatePicker datePicker;
    TimePicker timePicker;
    Spinner vehicleType;

    Button createOrder;

    private final static ArrayList<String> arrVehicleType = new ArrayList<>(Arrays.asList(
                                            TruckUtil.TYPE_BOX,TruckUtil.TYPE_FLATBED,TruckUtil.TYPE_LOG,
                                            TruckUtil.TYPE_MINI,TruckUtil.TYPE_REFRIGERATED,TruckUtil.TYPE_TANKER,
                                            TruckUtil.TYPE_TOW,TruckUtil.TYPE_VAN));

    //Current Date and Time
    Calendar calendar = Calendar.getInstance();
    Date currentDate = calendar.getTime();

    // Format the date and time using SimpleDateFormat
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

    String receiverNameText, goodTypeText, vehicleTypeText,
            weightText, widthText, lengthText, heightText, pickupLatLong, pickupLocName,

            dropOffLatLong, dropOffLocName;
    String datePickerText = dateFormat.format(currentDate);
    String timePickerText = timeFormat.format(currentDate);


    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_order);

        receiverName = findViewById(R.id.receiver_name);
        goodType = findViewById(R.id.good_type);
        weight = findViewById(R.id.weight);
        width = findViewById(R.id.width);
        length = findViewById(R.id.length);
        height = findViewById(R.id.height);
        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);
        vehicleType = findViewById(R.id.vehicle_type);
        createOrder = findViewById(R.id.create_order);

        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,arrVehicleType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleType.setAdapter(adapter);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment pickupLocFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.pickup_location);
        pickupLocFragment.setCountries("AU");

        AutocompleteSupportFragment dropOffFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.dropoff_location);
        dropOffFragment.setCountries("AU");

        // Specify the types of place data to return.
        pickupLocFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG));
        dropOffFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        pickupLocFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if(place.getLatLng()!=null){
                    pickupLatLong = place.getLatLng().latitude+","+place.getLatLng().longitude;
                    pickupLocName = place.getName();
                }

            }
        });

        dropOffFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if(place.getLatLng()!=null){
                    dropOffLatLong = place.getLatLng().latitude+","+place.getLatLng().longitude;
                    dropOffLocName = place.getName();
                }

            }
        });


        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                datePickerText = dateFormat.format(calendar.getTime());
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                timePickerText = timeFormat.format(calendar.getTime());
            }
        });

        vehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vehicleTypeText = arrVehicleType.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        createOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiverNameText =  receiverName.getText().toString();
                goodTypeText =  goodType.getText().toString();
                weightText =  weight.getText().toString();
                lengthText =  length.getText().toString();
                widthText=  width.getText().toString();
                heightText =  height.getText().toString();
                if(TextUtils.isEmpty(receiverNameText)){
                    showToast("Enter receiver name");
                    receiverName.setError("Enter receiver name");
                    return;
                }
                if(TextUtils.isEmpty(goodTypeText)){
                    showToast("Enter good type");
                    goodType.setError("Enter good type");
                    return;
                }
                if(TextUtils.isEmpty(weightText)){
                    showToast("Enter weight");
                    weight.setError("Enter weight");
                    return;
                }
                if(TextUtils.isEmpty(lengthText)){
                    showToast("Enter length");
                    length.setError("Enter length");
                    return;
                }
                if(TextUtils.isEmpty(widthText)){
                    showToast("Enter width");
                    width.setError("Enter width");
                    return;
                }
                if(TextUtils.isEmpty(heightText)){
                    showToast("Enter height");
                    height.setError("Enter height");
                    return;
                }
                if(pickupLatLong==null){
                    showToast("Select pickup location");
                    return;
                }
                if(dropOffLatLong==null){
                    showToast("Select drop-off location");
                    return;
                }
                Order order = new Order(UUID.randomUUID().toString(),receiverNameText, datePickerText, timePickerText, goodTypeText,
                        vehicleTypeText, pickupLatLong, pickupLocName, dropOffLatLong,dropOffLocName,
                        Float.parseFloat(weightText), Float.parseFloat(widthText),
                        Float.parseFloat(lengthText), Float.parseFloat(heightText));
                order.printOrder();
                OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(AddDeliveryOrderActivity.this);
                long insert = orderDatabaseHelper.insertOrder(order);
                if(insert>0){
                    showToast("New order created successfully");
                    Intent i = new Intent(AddDeliveryOrderActivity.this, MyOrdersActivity.class);
                    startActivity(i);
                }else{
                    showToast("Sorry! Could not create order. Please try again");
                }
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(AddDeliveryOrderActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}