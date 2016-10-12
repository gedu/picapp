package com.gemapps.picapp.ui.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.gemapps.picapp.helper.Utility;
import com.gemapps.picapp.helper.ViewUtil;

/**
 * Created by edu on 10/12/16.
 * Custom class to make a circular image, only for lollipop and above for now
 */
public class CircularImageView extends ImageView {

    public CircularImageView(Context context) {
        super(context);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        if(Utility.isLollipop()) {
            setOutlineProvider(ViewUtil.CIRCULAR_OUTLINE);
            setClipToOutline(true);
        }
    }



}
