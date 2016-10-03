package com.gemapps.picapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * Created by edu on 10/3/16.
 */

public class Utility {

    public static final String SHARED_PREFERENCE_KEY = "com.gemapps.picapp.SharedPreference";

    /**
     * Get the SharedPreferences.
     *
     * @param context The context so I can get Preferences.
     * @return  The private preference.
     */
    public static SharedPreferences getPrivatePreferences(Context context){
        return context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
    }

    /**
     * Get the SharedPreferences.Editor so you can edit preferences values.
     * Remember after put a new value call editor.commit()
     *
     * @param context The context so I can get Preferences
     * @return The private editor
     */
    public static SharedPreferences.Editor getPrivateEditor(Context context){
        return context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE).edit();
    }

    /**
     *
     * @return true if lollipop and above, otherwise false
     */
    public static boolean isLollipop(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
