package com.gemapps.picapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.helper.Utility;
import com.gemapps.picapp.helper.ViewUtil;
import com.gemapps.picapp.networking.FlickerUserPhotosClient;
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

    @BindView(R.id.activity_player) View mContainer;
    @BindView(R.id.user_icon_image) ImageView mUserIcon;
    @BindView(R.id.author_name) TextView mAuthorName;
    @BindView(R.id.author_bio) TextView mAuthorBio;
    @BindView(R.id.pic_faves) TextView mFaves;
    @BindView(R.id.pic_comments) TextView mComments;
    @BindView(R.id.author_shots) RecyclerView mRecyclerView;
    @BindView(R.id.progressBar) View mLoading;
    @BindView(R.id.author_description) View mDescContainer;
    @BindInt(R.integer.player_num_columns) int COLUMNS;
    @BindDimen(R.dimen.player_shots_elevation) int ELEVATION;

    private PicItem mPicItem;
    private UserItem mUserItem;

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

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, COLUMNS, LinearLayoutManager.VERTICAL, false));

        loadPhotos();
    }

    private void loadPhotos(){
        mLoading.setVisibility(View.VISIBLE);

        new FlickerUserPhotosClient().getUserPhotos(mUserItem.getId(), new FlickerUserPhotosClient.UserPhotosListener() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(ArrayList<UserPhotoItem> userPhotos) {

                UserPhotosAdapter adapter = new UserPhotosAdapter(userPhotos, PlayerActivity.this);

                ViewUtil.setPaddingTop(mRecyclerView, mDescContainer.getHeight());

                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setVisibility(View.VISIBLE);
                if(Utility.isLollipop()) mRecyclerView.setElevation(ELEVATION);
                mLoading.setVisibility(View.GONE);

            }
        });
    }
}
