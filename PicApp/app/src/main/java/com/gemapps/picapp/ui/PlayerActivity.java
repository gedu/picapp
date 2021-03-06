package com.gemapps.picapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.helper.Utility;
import com.gemapps.picapp.helper.ViewUtil;
import com.gemapps.picapp.networking.FlickrUserPhotosClient;
import com.gemapps.picapp.ui.adapters.UserPhotosAdapter;
import com.gemapps.picapp.ui.model.PicItem;
import com.gemapps.picapp.ui.model.UserItem;
import com.gemapps.picapp.ui.model.UserPhotoItem;

import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.BindView;

import static com.gemapps.picapp.ui.PhotoItemActivity.PIC_EXTRA_KEY;
import static com.gemapps.picapp.ui.PhotoItemActivity.USER_EXTRA_KEY;

public class PlayerActivity extends BaseActivity {

    private static final String USER_PICS_PREF = "picapp.user_pics_pref";

    @Nullable @BindView(R.id.dual_panel) View mDualPanel;
    @BindView(R.id.activity_player) View mContainer;
    @BindView(R.id.user_icon_image) ImageView mUserIcon;
    @BindView(R.id.author_name) TextView mAuthorName;
    @BindView(R.id.author_bio) TextView mAuthorBio;
    @BindView(R.id.pic_faves) TextView mFaves;
    @BindView(R.id.pic_comments) TextView mComments;
    @BindView(R.id.author_shots) RecyclerView mRecyclerView;
    @BindView(R.id.progressBar) View mLoading;
    @BindView(R.id.author_description) View mDescContainer;
    @BindView(R.id.empty_list_stub) ViewStub mEmptyView;
    @BindInt(R.integer.player_num_columns) int COLUMNS;
    @BindDimen(R.dimen.player_shots_elevation) int ELEVATION;

    private PicItem mPicItem;
    private UserItem mUserItem;
    private GridLayoutManager mLayoutManager;
    private boolean isDualPanel;

    public static Intent getInstance(Context context, UserItem userItem, PicItem picItem){

        Intent intent = new Intent(context, PlayerActivity.class);

        intent.putExtra(USER_EXTRA_KEY, userItem);
        intent.putExtra(PIC_EXTRA_KEY, picItem);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        isDualPanel = mDualPanel != null;

        Bundle bundle = getIntent().getExtras();
        mPicItem = bundle.getParcelable(PIC_EXTRA_KEY);
        mUserItem = bundle.getParcelable(USER_EXTRA_KEY);

        if(mUserItem != null) {
            mUserItem.loadIcon(this, mUserIcon);
        }

        if(mPicItem != null){
            mAuthorName.setText(mPicItem.getOwnerName());
            mAuthorBio.setText("");
            mFaves.setText(mPicItem.getFaves());
            mComments.setText(mPicItem.getComments());
        }

        mContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        mLayoutManager = new GridLayoutManager(this, COLUMNS, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(savedInstanceState == null)loadPhotos();
        else rebuildState(savedInstanceState);
    }

    private void rebuildState(Bundle savedInstanceState) {

        if(savedInstanceState.containsKey(USER_PICS_PREF)){
            ArrayList<UserPhotoItem> items = (ArrayList<UserPhotoItem>) savedInstanceState.get(USER_PICS_PREF);

            populateList(items);
        }else{
            loadPhotos();
        }
    }

    private void loadPhotos(){
        mLoading.setVisibility(View.VISIBLE);

        new FlickrUserPhotosClient().getUserPhotos(mUserItem.getId(), new FlickrUserPhotosClient.UserPhotosListener() {
            @Override
            public void onFailure() {

                mLoading.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(ArrayList<UserPhotoItem> userPhotos) {

                populateList(userPhotos);
            }
        });
    }

    private void populateList(ArrayList<UserPhotoItem> userPhotos){
        UserPhotosAdapter adapter = new UserPhotosAdapter(mPicItem.getOwnerName(), userPhotos, PlayerActivity.this);

        if(!isDualPanel) {
            addPaddingTopPadding();
            // forward clicks to the description view
            mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    final int firstVisible = mLayoutManager.findFirstVisibleItemPosition();

                    if(firstVisible > 0) return false;

                    if(mRecyclerView.getAdapter().getItemCount() == 0)
                        return mDescContainer.dispatchTouchEvent(event);

                    final RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForAdapterPosition(0);
                    if(vh == null) return false;

                    if(event.getY() < vh.itemView.getTop())
                        return mDescContainer.dispatchTouchEvent(event);

                    return false;
                }
            });
        }

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        if(Utility.isLollipop()) mRecyclerView.setElevation(ELEVATION);
        mLoading.setVisibility(View.GONE);
    }

    private void addPaddingTopPadding(){
        if(mDescContainer.getHeight() == 0){
            mDescContainer.getViewTreeObserver().addOnGlobalLayoutListener(mContainerLayoutListener);
        }else {
            ViewUtil.setPaddingTop(mRecyclerView, mDescContainer.getHeight());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(mRecyclerView.getAdapter() != null){
            outState.putParcelableArrayList(USER_PICS_PREF,
                    ((UserPhotosAdapter)mRecyclerView.getAdapter()).getItems());
        }

        super.onSaveInstanceState(outState);
    }

    private final ViewTreeObserver.OnGlobalLayoutListener mContainerLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            ViewUtil.setPaddingTop(mRecyclerView, mDescContainer.getHeight());
            mDescContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    };
}
