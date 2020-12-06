package com.example.wiffle_adeel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class connectAPI {
    @SerializedName("main")
    infoAPI main;

    public infoAPI getMain() {
        return main;
    }

    public void setMain(infoAPI main) {
        this.main = main;
    }

    @SerializedName("weather")
    List<Weather> weather;

    public List<Weather> getWeather() {
        return weather;
    }
}