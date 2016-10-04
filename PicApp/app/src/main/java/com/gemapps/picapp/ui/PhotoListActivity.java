package com.gemapps.picapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;

import com.gemapps.picapp.R;
import com.gemapps.picapp.helper.Utility;
import com.gemapps.picapp.networking.BaseHttpClient;
import com.gemapps.picapp.networking.FlickrClient;
import com.gemapps.picapp.ui.adapters.PicsAdapter;
import com.gemapps.picapp.ui.model.PicItem;
import com.gemapps.picapp.ui.model.QueryItem;
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
import static com.gemapps.picapp.ui.PhotoItemActivity.ITEM_EXTRA_KEY;

public class PhotoListActivity extends BaseActivity {

    private static final String TAG = "PhotoListActivity";
    private static final int SPECIFIC_GALLERY = 1;
    public static final String SAVED_QUERY_PREF = "saved_query";
    public static final String PHOTO_RECYCLER_LAYOUT = "picapp.photo_recycler_layout";

    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.query_stub) ViewStub mQueryStub;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.progressBar) View mProgressBar;

    private View mInflatedQuery;

    private LinearLayoutManager LINEAR_LAYOUT;
    private GridLayoutManager GRID_LAYOUT;

    private int mCurrentPage = 1;
    private String mQuery = "";

    private boolean mIsLoadingMore = false;
    private boolean mIsLinearLayout = true;

    PicsAdapter mPicsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        mIsLinearLayout = Utility.getPrivatePreferences(this).getBoolean(PHOTO_RECYCLER_LAYOUT, true);

        LINEAR_LAYOUT = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GRID_LAYOUT = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);

        GRID_LAYOUT.setSpanSizeLookup(mSpanLookup);

        mRecyclerView.setLayoutManager(mIsLinearLayout ? LINEAR_LAYOUT : GRID_LAYOUT);
        mRecyclerView.addOnScrollListener(mScrollListener);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mCurrentPage = 1;
                loadContent(mQuery);
            }
        });

        if(savedInstanceState == null) loadContent();
        else rebuildState(savedInstanceState);
    }

    private void rebuildState(Bundle savedInstanceState) {

        mQuery = savedInstanceState.getString(SAVED_QUERY_PREF);
        loadContent(mQuery);
        if(mQuery.length() > 0) showQueryPill();
    }

    private void loadContent(){
        loadContent(String.valueOf(mCurrentPage), "");
    }

    private void loadContent(String query){
        loadContent(String.valueOf(mCurrentPage), query);
    }

    private void loadContent(String page, String query){
        mProgressBar.setVisibility(View.VISIBLE);

        new FlickrClient().getPhotoList(page, query, new BaseHttpClient.CallbackResponse() {
            @Override
            public void onFailure() {
                //TODO: add error message
                mPicsAdapter.removeProgressItem();
                mIsLoadingMore = false;
                mSwipeRefreshLayout.setRefreshing(false);
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

                    if(mIsLoadingMore){
                        mPicsAdapter.removeProgressItem();
                        mPicsAdapter.addContent(picItems);
                    }else {
                        mPicsAdapter = new PicsAdapter(picItems, PhotoListActivity.this);
                        mPicsAdapter.setListener(mOnItemClickListener);
                        mPicsAdapter.updateImageHeight(mIsLinearLayout);
                        mRecyclerView.setAdapter(mPicsAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mIsLoadingMore = false;
                mSwipeRefreshLayout.setRefreshing(false);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(SAVED_QUERY_PREF, mQuery);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_list, menu);
        MenuItem item = menu.getItem(0);
        if(item.getItemId() == R.id.action_re_layout){
            item.setIcon(getResources().getDrawable(!mIsLinearLayout ?
                    R.drawable.ic_view_list_white_24px : R.drawable.ic_view_module_white_24px));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_re_layout:
                mIsLinearLayout = !mIsLinearLayout;
                Utility.getPrivateEditor(PhotoListActivity.this)
                        .putBoolean(PHOTO_RECYCLER_LAYOUT, mIsLinearLayout)
                        .apply();

                item.setIcon(getResources().getDrawable(!mIsLinearLayout ?
                        R.drawable.ic_view_list_white_24px : R.drawable.ic_view_module_white_24px));

                mRecyclerView.setLayoutManager(mIsLinearLayout ? LINEAR_LAYOUT : GRID_LAYOUT);
                mPicsAdapter.updateImageHeight(mIsLinearLayout);
                mPicsAdapter.notifyDataSetChanged();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == SPECIFIC_GALLERY){

            mQuery = data.getExtras().getString(SearchActivity.QUERY);
            loadContent(mQuery);
            showQueryPill();
        }
    }

    private void showQueryPill(){
        if(mInflatedQuery == null) mInflatedQuery = mQueryStub.inflate();
        else mInflatedQuery.setVisibility(View.VISIBLE);

        new QueryItem(mInflatedQuery, mQuery, new QueryItem.ClearListener() {
            @Override
            public void onClear() {
                mInflatedQuery.setVisibility(View.GONE);
                mQuery = "";
                loadContent();
            }
        });
    }

    private final PicsAdapter.OnItemClickListener mOnItemClickListener = new PicsAdapter.OnItemClickListener() {
        @Override
        public void onClick(PicItem item, View title, View image) {
            Intent intent = new Intent(PhotoListActivity.this, PhotoItemActivity.class);
            intent.putExtra(ITEM_EXTRA_KEY, item);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(PhotoListActivity.this,
                    Pair.create(image, "pic_image"));

            startActivity(intent, options.toBundle());
        }
    };

    @OnClick(R.id.fab)
    public void onFabClicked(View view){

        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SPECIFIC_GALLERY);
    }

    private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (dy > 0) {
                int totalItems = mRecyclerView.getLayoutManager().getItemCount();
                int lastVisibleItem = 0;

                if (mIsLinearLayout) {
                    lastVisibleItem = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                            .findLastVisibleItemPosition();
                } else {
                    lastVisibleItem = ((GridLayoutManager) mRecyclerView.getLayoutManager())
                            .findLastVisibleItemPosition();
                }

                //note: +3 how many of items to have below the current scroll position before loading more
                if (!mIsLoadingMore && totalItems <= (lastVisibleItem + 3)) {

                    mPicsAdapter.addProgressItem();
                    mCurrentPage++;
                    loadContent();
                    mIsLoadingMore = true;
                }

            }
        }
    };

    private final GridLayoutManager.SpanSizeLookup mSpanLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {

            return mIsLoadingMore &&
                    mPicsAdapter.getItemViewType(position) == PicsAdapter.VIEW_LOADING_TYPE ?
                    GRID_LAYOUT.getSpanCount() : 1;
        }
    };
}
