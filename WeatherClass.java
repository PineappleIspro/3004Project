package com.example.wiffle_adeel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherClass {
    @SerializedName("weather")
    List<Weather> main;


    public List<Weather> getWeather() {
        return main;
    }

}