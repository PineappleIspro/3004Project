package com.example.wiffle_adeel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
EditText place;
TextView display;

String apiKey = "d347328678a5eb693250b4aa687d02a8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        place = findViewById(R.id.place);
        display = findViewById(R.id.display);
    }
    public void getWeather(View v){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        API api = retrofit.create(API.class);
        Call<MainClass> API = api.getWeather(place.getText().toString().trim(),apiKey);
        API.enqueue(new Callback<MainClass>() {
            @Override
            public void onResponse(Call<MainClass> call, Response<MainClass> response) {

                MainClass data = response.body();
                Main main = data.getMain();
                Double Kalv_Temp = main.getTemp();
                Integer Celc_Temp = (int) (Kalv_Temp - 273.15); //calv to celc
                display.setText(String.valueOf(Celc_Temp));
            }

            @Override
            public void onFailure(Call<MainClass> call, Throwable t) {

            }
        });

    }
}