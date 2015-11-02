package com.haley.may.mayapp.System;

/**
 * Created by lenovo on 2015/10/21.
 */
public class Settings {

    private static Settings instance = new Settings();

    public Settings(){

    }

    public static Settings getInstance() {
        return instance;
    }


}
