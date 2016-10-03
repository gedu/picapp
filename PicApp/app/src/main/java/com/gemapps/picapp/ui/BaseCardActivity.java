package com.gemapps.picapp.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;

import com.gemapps.picapp.R;
import com.gemapps.picapp.helper.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by edu on 10/3/16.
 */

public class BaseCardActivity extends AppCompatActivity {

    private static final String TAG = "BaseCardActivity";

    protected final static Interpolator ACC_DEC_INTERPOLATOR = new FastOutLinearInInterpolator();
    private static final String CENTER_X_KEY = "centerX";

    @BindView(R.id.trans_bg_view)
    View mBackground;
    @BindView(R.id.card_panel_container) protected View mCardPanel;
    private int mCenterX = 0;

    /**
     * Create a {@link Intent} instance with the center X param, to use later in the animation
     * @param view The view to which get the center x
     * @param className The activity to start
     * @return
     */
    public static Intent getInstance(View  view, Class<?> className){

        Intent intent = new Intent(view.getContext(), className);

        intent.putExtra(CENTER_X_KEY, getCenterX(view));

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras() != null) {

            mCenterX = (int) getIntent().getExtras()
                    .getFloat(CENTER_X_KEY);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        doEntryAnimation();
        overrideTrans();
    }

    /**
     * To make the animation works this should be called after the doEntryAnimation
     * on the onCreate method
     */
    protected void overrideTrans(){
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isFinishing()) dismiss(null);
    }

    @Override
    public void onBackPressed() {
        dismiss(null);
    }

    /**
     * Should be called on the onCreate function at the end before {#link overrideTrans}
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void doEntryAnimation(){

        if(!Utility.isLollipop()) return;

        mBackground.animate()
                .alpha(1)
                .setDuration(500L)
                .setInterpolator(ACC_DEC_INTERPOLATOR)
                .start();

        mCardPanel.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {

                mCardPanel.getViewTreeObserver().removeOnPreDrawListener(this);

                int revealRadius = getRevealSize();

                Animator show = ViewAnimationUtils.createCircularReveal(mCardPanel,
                        mCardPanel.getWidth()/2,
                        mCardPanel.getHeight()/2,
                        0f, revealRadius);
                show.setDuration(350L);
                show.setInterpolator(ACC_DEC_INTERPOLATOR);
                show.start();

                return false;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void doExitAnimation(){

        if(!Utility.isLollipop()) return;

        mBackground.animate()
                .alpha(0f)
                .setDuration(200L)
                .setInterpolator(ACC_DEC_INTERPOLATOR)
                .start();

        mCardPanel.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {

                mCardPanel.getViewTreeObserver().removeOnPreDrawListener(this);

                int revealRadius = getRevealSize();

                Animator show = ViewAnimationUtils.createCircularReveal(mCardPanel,
                        mCardPanel.getWidth()/2,
                        mCardPanel.getHeight()/2,
                        revealRadius, 0f);
                show.setDuration(350L);
                show.setInterpolator(ACC_DEC_INTERPOLATOR);
                show.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mCardPanel.setVisibility(View.INVISIBLE);
                        ActivityCompat.finishAfterTransition(BaseCardActivity.this);
                    }
                });
                show.start();

                return false;
            }
        });
    }

    @OnClick(R.id.trans_bg_view)
    public void onBgClicked(){
        dismiss(null);
    }

    protected void dismiss(View view){
        if (Utility.isLollipop()) {
            doExitAnimation();
        }else{
            ActivityCompat.finishAfterTransition(this);
        }
    }

    public static float getCenterX(View view){
        return (view.getX() + view.getWidth() / 2);
    }

    protected int getRevealSize(){
        return mCardPanel.getWidth();
    }

    protected int getCenterX(){
        return mCenterX;
    }
}
