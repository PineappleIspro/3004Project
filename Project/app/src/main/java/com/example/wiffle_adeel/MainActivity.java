package com.example.wiffle_adeel;

import android.Manifest;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;


import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final String CHANNEL_ID = "personal_notifications";
    private final int Notification_ID = 001;
    Button btlocation;
    TextView tvlatitude,tvlongitude, tvcity;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //assign variable
        btlocation=findViewById(R.id.bt_location);
        tvlatitude=findViewById(R.id.tv_latitude);
        tvlongitude=findViewById(R.id.tv_longitude);
        tvcity=findViewById(R.id.tv_city);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        btlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permissions
                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ){
                    getLocation();
                }
                else{
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

                }
            }
        });

    }

    private void getLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE
           );
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled((LocationManager.NETWORK_PROVIDER))) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    final Location location = task.getResult();
                    if(location !=null){
                        tvlatitude.setText(String.valueOf(location.getLatitude()));

                        tvlongitude.setText(String.valueOf(location.getLongitude()));
                    }
                    else{
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult){
                                Location location1 = locationResult.getLastLocation();

                                tvlatitude.setText((String.valueOf(location1.getLatitude())));

                                tvlongitude.setText(String.valueOf(location1.getLongitude()));

                                tvcity.setText((getCityName(location1.getLatitude(),location1.getLongitude())));
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });

        }else{
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
    //get nearest city with lat and lon
    public String getCityName(double lat, double lon){
        String curCity= "";

        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        List<Address> addressList;
        try{
            addressList = geocoder.getFromLocation(lat, lon, 10);
            if(addressList.size() >0){
                for(Address adr: addressList){
                    if(adr.getLocality() != null && adr.getLocality().length() >0 ){
                        curCity=adr.getLocality();
                        break;
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();

        }
        return curCity;

    }
    public void displayNotification(View view){

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_sms_notification);
        builder.setContentTitle("Weather Update");
        builder.setContentText("TEST");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);

        NotificationManagerCompat notifyManagerCompact=NotificationManagerCompat.from(this);
        notifyManagerCompact.notify(Notification_ID,builder.build());
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "Personal Notifications";
            String description = "include all personal notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }
}

