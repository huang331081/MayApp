package com.haley.may.mayapp.Interface;

import android.os.Bundle;

import com.haley.may.mayapp.Manager.StyleManager;

/**
 * Created by lenovo on 2015/10/23.
 */
public interface IStyleChange {

    void setStyle(StyleManager.StyleInfo previousStyle,StyleManager.StyleInfo style);
}
