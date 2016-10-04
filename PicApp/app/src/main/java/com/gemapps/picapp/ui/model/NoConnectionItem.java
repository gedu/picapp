package com.gemapps.picapp.ui.model;

import android.util.Log;
import android.view.View;

import com.gemapps.picapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by edu on 10/4/16.
 */

public class NoConnectionItem {
    private static final String TAG = "NoConnectionItem";

    public interface TryAgainListener {
        void onTry();
    }
    @BindView(R.id.contentPanel) View mContainer;

    private TryAgainListener mListener;

    public NoConnectionItem(View view, TryAgainListener listener) {
        ButterKnife.bind(this, view);

        mListener = listener;
    }

    public void showView(){
        mContainer.setVisibility(View.VISIBLE);
    }

    public void hideView(){
        mContainer.setVisibility(View.GONE);
    }

    @OnClick(R.id.try_again_button)
    public void onTryClicked(){
        Log.d(TAG, "onTryClicked() called");
        if(mListener != null)
            mListener.onTry();
    }
}
