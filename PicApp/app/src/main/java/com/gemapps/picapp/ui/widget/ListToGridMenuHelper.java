package com.gemapps.picapp.ui.widget;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.gemapps.picapp.R;
import com.gemapps.picapp.helper.Utility;

/**
 * Created by edu on 10/20/16.
 */

public class ListToGridMenuHelper {

    private static Drawable mDrawable;

    public static Drawable getDrawable(Context context, boolean isList){
        if(!Utility.isLollipop()) {
            mDrawable = context.getResources().getDrawable(isList ?
                    R.drawable.ic_view_list_white_24px : R.drawable.ic_view_module_white_24px);
            return mDrawable;
        }else{

            mDrawable = ContextCompat.getDrawable(context, isList ?
                     R.drawable.vc_list_drawable : R.drawable.vc_grid_drawable);
            return mDrawable;
        }
    }

    public static Drawable morph(Context context, boolean isList){
        if(!Utility.isLollipop()){
            return getDrawable(context, isList);
        }else{

            mDrawable = ContextCompat.getDrawable(context, isList ?
                    R.drawable.avd_grid_to_list : R.drawable.avd_list_to_grid);

            ((Animatable)mDrawable).start();
            return mDrawable;
        }
    }
}
