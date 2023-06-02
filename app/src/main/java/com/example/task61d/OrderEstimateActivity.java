package com.example.task61d;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.task61d.data.OrderDatabaseHelper;
import com.example.task61d.data.TruckDatabaseHelper;
import com.example.task61d.model.Order;
import com.example.task61d.model.Truck;
import com.example.task61d.util.OrderUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OrderEstimateActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    private GeofencingClient geofencingClient;
    private GeofenceManager geofenceManager;
    TextView pickupLoc, dropOffLoc, travelTime, fare;
    Button bookBtn, callDriverBtn;
    private static final int REQUEST_CODE = 1;
    public static final int CALL_REQUEST_CODE = 3;
    PaymentSheet paymentSheet;
    String customerID, ephemeralKey, clientSecret;
    double paymentAmount;
    private Stripe stripe;
    Order order = null;
    Truck truck = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_estimate);

        // Check if the location permission is granted
        if (isLocationPermissionGranted()) {
            initializeMap();
        } else {
            requestLocationPermission();
        }

        pickupLoc = findViewById(R.id.pickup_location);
        dropOffLoc = findViewById(R.id.dropoff_location);
        travelTime = findViewById(R.id.travel_time);
        fare = findViewById(R.id.fare);
        bookBtn = findViewById(R.id.book_btn);
        callDriverBtn = findViewById(R.id.call_driver_btn);

        Intent i = getIntent();
        String id;
        id = i.getStringExtra(OrderUtil.ORDER_ID);

        OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(this);
        TruckDatabaseHelper truckDatabaseHelper = new TruckDatabaseHelper(this);
        order = orderDatabaseHelper.getOrderDetail(id);
        truck = truckDatabaseHelper.getTruckByType(order.getVehicleType());

        initializePayment();

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceManager = new GeofenceManager(this);

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use credit card number: 4242 4242 4242 4242  with any expiry date, CVC and address
                paymentFlow();
            }
        });

        callDriverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall();
            }
        });
    }

    //Code for calculating directions and fare on google Maps
    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        // Extracting latlng double value from string
        String[] originArray = order.getPickupLatLong().split(",");
        String[] destArray = order.getDropOffLatLong().split(",");

        LatLng originLatLng = new LatLng(Double.parseDouble(originArray[0]), Double.parseDouble(originArray[1]));
        LatLng destLatLng = new LatLng(Double.parseDouble(destArray[0]), Double.parseDouble(destArray[1]));

        gMap.addMarker(new MarkerOptions().position(originLatLng).title(order.getPickupLocName()));
        gMap.addMarker(new MarkerOptions().position(destLatLng).title(order.getDropOffLocName()));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(originLatLng);
        builder.include(destLatLng);
        LatLngBounds bounds = builder.build();

        // Calculate padding for the camera movement (optional)
        int padding = 100;

        // Create a CameraUpdate object to move and zoom the camera to fit the bounds
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        // Move and zoom the camera to the calculated bounds
        gMap.moveCamera(cameraUpdate);
        // Draw polyline between origin and destination
        drawPolyline(originLatLng, destLatLng);
    }

    private void drawPolyline(LatLng originLatLng, LatLng destLatLng) {
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(BuildConfig.MAPS_API_KEY)
                .build();
        DirectionsApiRequest directions = DirectionsApi.newRequest(geoApiContext)
                .origin(new com.google.maps.model.LatLng(originLatLng.latitude, originLatLng.longitude))
                .destination(new com.google.maps.model.LatLng(destLatLng.latitude, destLatLng.longitude))
                .mode(TravelMode.DRIVING);
        try {
            DirectionsResult result = directions.await();
            if (result.routes != null && result.routes.length > 0) {
                DirectionsRoute route = result.routes[0];
                DirectionsLeg legs = result.routes[0].legs[0];
                Distance distance = legs.distance;
                double distanceKm = (double) (distance.inMeters / 1000);
                double distanceFare = calculateFare(distanceKm);

                Duration duration = legs.duration;

                com.google.maps.model.LatLng[] points = route.overviewPolyline.decodePath().toArray(new com.google.maps.model.LatLng[0]);

                PolylineOptions polylineOptions = new PolylineOptions();
                for (com.google.maps.model.LatLng point : points) {
                    LatLng latLng = new LatLng(point.lat, point.lng);
                    polylineOptions.add(latLng);
                }

                polylineOptions.color(Color.BLUE);
                polylineOptions.width(10);

                addDestGeofence(destLatLng);

                gMap.addPolyline(polylineOptions);
                pickupLoc.setText(order.getPickupLocName());
                dropOffLoc.setText(order.getDropOffLocName());
                fare.setText("$"+ distanceFare);
                paymentAmount = distanceFare;
                travelTime.setText(duration.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressLint("MissingPermission")
    private void addDestGeofence(LatLng destLatLng) {
        Geofence geofence = geofenceManager.getGeofence("drop-off", destLatLng, 200, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceManager.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceManager.getPendingIntent();


        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceManager.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(destLatLng);
        circleOptions.radius(200);
        circleOptions.strokeColor(Color.DKGRAY);
        circleOptions.fillColor(Color.LTGRAY);
        circleOptions.strokeWidth(4);
        gMap.addCircle(circleOptions);
    }

    public double calculateFare(double distanceKm){
        double farePerKm = 2.5;
        double fare = farePerKm * distanceKm;
        return fare;
    }

    //Code for calling driver
    private void makePhoneCall() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},CALL_REQUEST_CODE);
        }else {
            String dial = "tel:"+truck.getPhoneNumber();
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    //Code for making payment
    private void initializePayment() {
        PaymentConfiguration.init(this, BuildConfig.PUBLISHABLE_KEY);
        paymentSheet = new PaymentSheet(this,paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            customerID = object.getString("id");
                            getEphemeralKey(customerID);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+BuildConfig.STRIPE_SECRET_KEY);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(OrderEstimateActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getEphemeralKey(String customerID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            ephemeralKey = object.getString("id");
                            Log.d("ephemeralKey", ephemeralKey);
                            getClientSecret(customerID, ephemeralKey);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+BuildConfig.STRIPE_SECRET_KEY);
                headers.put("Stripe-Version","2022-11-15");
                return headers;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(OrderEstimateActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getClientSecret(String customerID, String ephemeralKey) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            clientSecret = object.getString("client_secret");
                            Log.d("clienSecret", clientSecret);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+BuildConfig.STRIPE_SECRET_KEY);
                return headers;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", String.valueOf((int) (paymentAmount * 100)));
                params.put("currency", "aud");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(OrderEstimateActivity.this);
        requestQueue.add(stringRequest);
    }

    private void paymentFlow() {
        paymentSheet.presentWithPaymentIntent(
                clientSecret, new PaymentSheet.Configuration("Truck Share",new PaymentSheet.CustomerConfiguration(
                        customerID,
                        ephemeralKey
                ))
        );
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            showToast("Payment Successful");
        }
    }

    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMap();
            } else {
                // Permission denied, handle accordingly (e.g., show a message, close the activity)
                finish();
            }
        }
        if (requestCode == CALL_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                // Permission denied, handle accordingly (e.g., show a message, close the activity)
                showToast("Permission Denied!");
            }
        }
    }

    private void showToast(String s) {
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
    }

}