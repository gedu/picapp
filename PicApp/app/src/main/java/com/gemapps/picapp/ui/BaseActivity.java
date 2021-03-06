package com.gemapps.picapp.ui;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.gemapps.picapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by edu on 9/30/16.
 * Wrapper class for Butter Knife
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    @Nullable @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);

        Log.d(TAG, "setContentView: "+mToolbar);
        if(mToolbar != null) setSupportActionBar(mToolbar);
    }

    /**
     * Set the Home/Up button in the Toolbar
     */
    protected void setUpButtonToolbar(){
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void setToolbarTitle(String title){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
    }
}
