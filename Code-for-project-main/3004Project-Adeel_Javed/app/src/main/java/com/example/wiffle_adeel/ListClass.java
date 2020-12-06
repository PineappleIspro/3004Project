package com.example.wiffle_adeel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;

public class ListClass {
    @SerializedName("list")
    private List<List1> main;


    public List<List1> getForecast() {
        return main;
    }


}