package com.gemapps.picapp.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
    @BindView(R.id.progressBar) View mProgressBar;

    private LinearLayoutManager LINEAR_LAYOUT;
    private GridLayoutManager GRID_LAYOUT;

    private boolean isLinearLayout = true;

    PicsAdapter mPicsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        LINEAR_LAYOUT = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GRID_LAYOUT = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(LINEAR_LAYOUT);
        loadContent();
    }

    private void loadContent(){
        mProgressBar.setVisibility(View.VISIBLE);

        new FlickrClient().getPhotoList(new BaseHttpClient.CallbackResponse() {
            @Override
            public void onFailure() {
                //TODO: add error message
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess(String response) {

                try {
                    JSONObject photoObj = new JSONObject(response);
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

                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_re_layout:
                isLinearLayout = !isLinearLayout;
                item.setIcon(getResources().getDrawable(isLinearLayout ?
                        R.drawable.ic_view_list_white_24px : R.drawable.ic_view_module_white_24px));
                mRecyclerView.setLayoutManager(isLinearLayout ? LINEAR_LAYOUT : GRID_LAYOUT);
                mPicsAdapter.updateImageHeigth(isLinearLayout);
                mPicsAdapter.notifyDataSetChanged();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @OnClick(R.id.fab)
    public void onFabClicked(View view){
        Snackbar.make(view, "Calling to Flickr", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();


    }
}
