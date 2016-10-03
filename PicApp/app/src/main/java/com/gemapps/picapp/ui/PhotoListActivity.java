package com.gemapps.picapp.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.gemapps.picapp.R;
import com.gemapps.picapp.networking.BaseHttpClient;
import com.gemapps.picapp.networking.FlickrClient;
import com.gemapps.picapp.ui.adapters.PicsAdapter;
import com.gemapps.picapp.ui.model.PicItem;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gemapps.picapp.networking.FlickrClient.PHOTOS_KEY;
import static com.gemapps.picapp.networking.FlickrClient.PHOTO_KEY;

public class PhotoListActivity extends BaseActivity {

    private static final String TAG = "PhotoListActivity";

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    PicsAdapter mPicsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @OnClick(R.id.fab)
    public void onFabClicked(View view){
        Snackbar.make(view, "Calling to Flickr", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        new FlickrClient().getPhotoList(new BaseHttpClient.CallbackResponse() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(String response) {

                try {
                    JSONObject photoObj = new JSONObject(response);
                    Log.d(TAG, "onSuccess: "+photoObj);
                    JSONObject photoContent = new JSONObject(photoObj.getString(PHOTOS_KEY));
                    JSONArray photos = new JSONArray(photoContent.getString(PHOTO_KEY));

                    int length = photos.length();
                    Gson gson = new Gson();
                    List<PicItem> picItems = new ArrayList<PicItem>();
                    for (int i = 0; i < length; i++) {

                        PicItem picItem = gson.fromJson(photos.getString(i), PicItem.class);
                        picItems.add(picItem);
                    }

                    mPicsAdapter = new PicsAdapter(picItems, PhotoListActivity.this);
                    mRecyclerView.setAdapter(mPicsAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
