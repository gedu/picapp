package com.gemapps.picapp.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.ui.model.PicItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class PhotoItemActivity extends BaseActivity {

//    @BindView(R.id.pic_title_text) TextView mTitleView;
    @BindView(R.id.pic_image) ImageView mImageView;
    @BindView(R.id.pic_comments) TextView mCommentsView;
    @BindView(R.id.pic_faves) TextView mFavesView;

    private PicItem mPicItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_item);

        setUpButtonToolbar();
        mPicItem = (PicItem) getIntent().getExtras().getSerializable("item");

//        mTitleView.setText(mPicItem.getTitle());
        setToolbarTitle(mPicItem.getTitle());
        mCommentsView.setText(mPicItem.getComments());
        mFavesView.setText(mPicItem.getFaves());
        Picasso.with(this).load(mPicItem.getPicUrl()).into(mImageView);
    }
}
