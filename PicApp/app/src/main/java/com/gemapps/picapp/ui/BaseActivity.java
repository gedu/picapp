package com.gemapps.picapp.ui;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gemapps.picapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by edu on 9/30/16.
 * Wrapper class for Butter Knife
 */

public class BaseActivity extends AppCompatActivity {

    @Nullable @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);

        if(mToolbar != null) setSupportActionBar(mToolbar);
    }

    /**
     * Set the Home/Up button in the Toolbar
     */
    protected void setUpButtonToolbar(){
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
