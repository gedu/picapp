package com.gemapps.picapp.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.helper.CircleTransform;
import com.gemapps.picapp.networking.FlickerUserClient;
import com.gemapps.picapp.ui.model.PicItem;
import com.gemapps.picapp.ui.model.UserItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class PhotoItemActivity extends BaseActivity {

    private static final String TAG = "PhotoItemActivity";
    public static final String ITEM_EXTRA_KEY = "picapp_item";

    @BindView(R.id.user_icon_image) ImageView mIconView;
    @BindView(R.id.user_name_text) TextView mUsernameView;
    @BindView(R.id.pic_title_text) TextView mTitleView;
    @BindView(R.id.pic_comments) TextView mCommentView;
    @BindView(R.id.pic_faves) TextView mFavesView;
    @BindView(R.id.pic_image) ImageView mImageView;
    @BindView(R.id.pic_date_taken) TextView mPicTakenDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_item);

        setUpButtonToolbar();

        PicItem picItem = (PicItem) getIntent().getExtras().getSerializable(ITEM_EXTRA_KEY);
        new FlickerUserClient().getUserInfo(picItem.getOwnerId(), new FlickerUserClient.UserListener() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(UserItem userItem) {
                Picasso.with(PhotoItemActivity.this)
                        .load(userItem.getIconUrl())
                        .placeholder(R.drawable.ic_buddy)
                        .error(R.drawable.ic_buddy)
                        .transform(new CircleTransform()).into(mIconView);
            }
        });

        mUsernameView.setText(picItem.getOwnerName());
        mPicTakenDateView.setText(picItem.getPicDateTaken());
        mTitleView.setText(picItem.getTitle());
        mCommentView.setText(picItem.getComments());
        mFavesView.setText(picItem.getFaves());

        Picasso.with(this).load(picItem.getPicUrl()).into(mImageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
