package com.gemapps.picapp.ui.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.gemapps.picapp.R;

/**
 * Created by edu on 10/22/16.
 */

public class FixScaleImageVIew extends ImageView {

    private static final String TAG = "FixScaleImageVIew";
    private static final float NORMAL_SCALE = 1f;

    private final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator(2f);
    private final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator(2f);

    private float mScaleValue = NORMAL_SCALE;

    private Drawable mOriginalBitmap = null;
    private Drawable mBlurBitmap = null;

    public FixScaleImageVIew(Context context) {
        super(context);
    }

    public FixScaleImageVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FixScaleImageVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FixScaleImageVIew(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.FixScaleImageVIew, 0, 0);

        try{
            mScaleValue = typedArray.getFloat(R.styleable.FixScaleImageVIew_scaleValue, NORMAL_SCALE);
        }finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setPivotX(0);
        setPivotY(0);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if(drawable != null) {
            mOriginalBitmap = drawable;
            mBlurBitmap = new BitmapDrawable(blurRenderScript(((BitmapDrawable) drawable).getBitmap(), 10));
        }
    }

    public void zoomOut(){

        if(mOriginalBitmap != null) {
            super.setImageDrawable(mOriginalBitmap);
        }

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(this, View.SCALE_X, NORMAL_SCALE))
                .with(ObjectAnimator.ofFloat(this, View.SCALE_Y, NORMAL_SCALE));
        set.setInterpolator(DECELERATE_INTERPOLATOR);
        set.start();
    }

    public void zoomIn(){

        if(mBlurBitmap != null) {
            super.setImageDrawable(mBlurBitmap);
        }

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(this, View.SCALE_X, NORMAL_SCALE, mScaleValue))
                .with(ObjectAnimator.ofFloat(this, View.SCALE_Y, mScaleValue));
        set.setInterpolator(ACCELERATE_INTERPOLATOR);
        set.start();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {

        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(getContext());

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }
}
