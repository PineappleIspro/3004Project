package com.example.wiffle_adeel;

import com.google.gson.annotations.SerializedName;

public class MainClass {
    @SerializedName("main")
    Main main;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
