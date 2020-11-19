package com.example.wiffle_adeel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    EditText place;
    TextView weatherMain;
    private Button plannerButton;
    TextView weatherDescription;


    String apiKey = "d347328678a5eb693250b4aa687d02a8";
    private final String CHANNEL_ID = "personal_notifications";
    private final int Notification_ID = 001;
    Button btlocation;
    TextView tvlatitude,tvlongitude, tvcity;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleDateFormat formatter= new SimpleDateFormat("MMMM-dd-yyyy 'at' HH:mm z");
        SimpleDateFormat plannerFormatter= new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        String dateString = formatter.format(date);
        String plannerDate = plannerFormatter.format(date);
        setContentView(R.layout.activity_main);

        //assign variable
        btlocation=findViewById(R.id.locationButton);
        tvlatitude=findViewById(R.id.tv_latitude);
        tvlongitude=findViewById(R.id.tv_longitude);
        tvcity=findViewById(R.id.tv_city);
        plannerButton = (Button) findViewById(R.id.plannerButton);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        TextView dateTime = (TextView) findViewById(R.id.dateTime);
        dateTime.setText(dateString);
        dateTime.setTextSize(15);

        weatherMain = findViewById(R.id.weatherMain);
        weatherMain.setTextSize(15);



        weatherDescription = findViewById(R.id.Description);
        weatherDescription.setTextSize(15);


        getWeather();

        final TextView tripTitle = (TextView) findViewById(R.id.trips_title);
        final String tripsWord = " Your Trips";
        tripTitle.setText(tripsWord);
        tripTitle.setTextSize(20);

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
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

                }
            }
        });

        plannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlanner();

            }
        });

    }

    public void openPlanner(){
        Intent intent = new Intent(this, Planner.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0]+ grantResults[1]
                == PackageManager.PERMISSION_GRANTED)){
            getLocation();
        }else{
            Toast.makeText(getApplicationContext(), "Permission denied.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")

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

                        tvcity.setText((getCityName(location.getLatitude(),location.getLongitude())));

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
                    getWeather();
                }
            });

        }else{
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public void getWeather(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        API api = retrofit.create(API.class);
        Call<connectAPI> API = api.getWeather(tvcity.getText().toString(),apiKey);
        API.enqueue(new Callback<connectAPI>() {
            @Override
            public void onResponse(Call<connectAPI> call, Response<connectAPI> response) {

                connectAPI data = response.body();
                infoAPI main = data.getMain();
                Double Kalv_Temp = main.getTemp();
                Integer Celc_Temp = (int) (Kalv_Temp - 273.15); //calv to celc
                String weatherString = String.valueOf(Celc_Temp) + " ℃ in " + tvcity.getText().toString() ;
                weatherMain.setText(weatherString);
            }


            @Override
            public void onFailure(Call<connectAPI> call, Throwable t) {

            }
        });

    }



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
        //builder.setSmallIcon(R.drawable.ic_sms_notification);
        builder.setContentTitle("Weather Update");
        builder.setContentText("TEST");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);

        NotificationManagerCompat notifyManagerCompact= NotificationManagerCompat.from(this);
        notifyManagerCompact.notify(Notification_ID,builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Personal Notifications";
            String description = "include all personal notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

        }

    }
}