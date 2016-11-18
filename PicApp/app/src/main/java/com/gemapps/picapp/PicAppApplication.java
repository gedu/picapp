package com.gemapps.picapp;

import android.app.Application;

import com.gemapps.picapp.ui.model.PicBusEventIndex;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by edu on 11/18/16.
 *
 * To handle first initializations
 */
public class PicAppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        EventBus.builder()
                .addIndex(new PicBusEventIndex())
                .installDefaultEventBus();
    }
}
