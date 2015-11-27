package com.haley.may.mayapp.Style;

/**
 * Created by haley on 2015/11/23.
 */
public interface IStyleChangedEvent {

    void onEventMainThread(StyleManager.StyleEventInfo styleEventInfo);
}
