package com.gemapps.picapp.ui.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.gemapps.picapp.R;

/**
 * Created by edu on 10/28/16.
 *
 * A text view which will translate (and scale) given a target view (like anchor)
 * and can translate back
 */
public class TranslationTextView extends TextView {

    private static final String TAG = "TranslationTextView";

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator(2f);
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator(2f);

    private static final float NORMAL_SCALE = 1f;
    private static final float TO_START_POSITION = 0f;

    private final AnimatorSet mForwardViewSet = new AnimatorSet();
    private final  AnimatorSet mBackViewSet = new AnimatorSet();

    private float mScaleValue = 2f;
    private int mAnchorViewId;

    private View mAnchorView;

    public TranslationTextView(Context context) {
        super(context);
    }

    public TranslationTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TranslationTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TranslationTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){

        TypedArray typedArray = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.TranslationTextView, 0 , 0);

        try{

            mScaleValue = typedArray.getFloat(R.styleable.TranslationTextView_scaleText, 2f);
            mAnchorViewId = typedArray.getResourceId(R.styleable.TranslationTextView_anchor, -1);

        }finally {
            typedArray.recycle();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mAnchorView = getRootView().findViewById(mAnchorViewId);
        mAnchorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                setupAnimations();
                mAnchorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        Display display = getDisplay();
        Point size = new Point();
        display.getSize(size);
    }

    private void setupAnimations(){

        ObjectAnimator xAnimFor = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, mAnchorView.getX()/2f);
        xAnimFor.setStartDelay(50);

        mForwardViewSet.play(xAnimFor)
                .with(ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, mAnchorView.getY()/2f))
                .with(ObjectAnimator.ofFloat(this, View.SCALE_X, NORMAL_SCALE, mScaleValue))
                .with(ObjectAnimator.ofFloat(this, View.SCALE_Y, NORMAL_SCALE, mScaleValue));
        mForwardViewSet.setInterpolator(DECELERATE_INTERPOLATOR);

        ObjectAnimator yAnimBack = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, TO_START_POSITION);
        yAnimBack.setStartDelay(50);

        mBackViewSet.play(ObjectAnimator.ofFloat(this, View.TRANSLATION_X, TO_START_POSITION))
                .with(yAnimBack)
                .with(ObjectAnimator.ofFloat(this, View.SCALE_X, NORMAL_SCALE))
                .with(ObjectAnimator.ofFloat(this, View.SCALE_Y, NORMAL_SCALE));
        mBackViewSet.setInterpolator(ACCELERATE_INTERPOLATOR);
//        mBackViewSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//
//                Log.d(TAG, "getX: "+getX());
//                Log.d(TAG, "getY: "+getY());
//                Log.d(TAG, "getWidth: "+getWidth());
//                Log.d(TAG, "getHeight: "+getHeight());
//
//            }
//        });
    }

    public void translateForward(){

        mForwardViewSet.start();
    }

    public void translateBack(){
        mBackViewSet.start();
    }
}
