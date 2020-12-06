package com.example.wiffle_adeel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
     @GET("weather")
     Call<connectAPI> getWeather(@Query("q") String cityname, @Query("appid") String apikey);

     @GET("forecast")
     Call<ListClass> FiveDayForecast(@Query("q") String cityname, @Query("appid") String apikey);

}

