package com.gemapps.picapp.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.gemapps.picapp.R;
import com.gemapps.picapp.networking.FlickrClient;

import butterknife.OnClick;

public class PhotoListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
    }

    @OnClick(R.id.fab)
    public void onFabClicked(View view){
        Snackbar.make(view, "Calling to Flickr", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        new FlickrClient().getPhotoList();
    }
}
