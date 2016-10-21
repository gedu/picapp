package com.gemapps.picapp.helper;

import android.graphics.Outline;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * Created by edu on 10/12/16.
 */
public class ViewUtil {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static final ViewOutlineProvider CIRCULAR_OUTLINE = new ViewOutlineProvider() {
        @Override
        public void getOutline(View view, Outline outline) {
            outline.setOval(view.getPaddingLeft(),
                    view.getPaddingTop(),
                    view.getWidth() - view.getPaddingRight(),
                    view.getHeight() - view.getPaddingBottom());
        }
    };


    /**
     * Set a new top padding
     * @param view The view to set the new top padding
     * @param paddingTop The new padding value
     */
    public static void setPaddingTop(View view, int paddingTop){

        if(Utility.isJellyBean17()){
            view.setPaddingRelative(view.getPaddingStart(),
                    paddingTop,
                    view.getPaddingEnd(),
                    view.getPaddingBottom());
        }else{
            ViewCompat.setPaddingRelative(view, view.getPaddingLeft(),
                    paddingTop,
                    view.getPaddingRight(),
                    view.getPaddingBottom());
        }
    }
}
