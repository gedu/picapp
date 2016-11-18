package com.gemapps.picapp.ui.model;

import android.os.Bundle;

/**
 * Created by edu on 11/18/16.
 */

public class PicBusEvent {

    public static final int LOAD_SUCCESS = 1;

    private int mMode;
    private Bundle mBundle;

    public PicBusEvent(int mode) {
        mMode = mode;
    }

    public int getMode() {
        return mMode;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public void setBundle(Bundle bundle) {
        mBundle = bundle;
    }
}
