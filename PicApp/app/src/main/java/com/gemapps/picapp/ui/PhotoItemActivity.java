package com.gemapps.picapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.ui.model.PicItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

public class PhotoItemActivity extends BaseActivity {

    @BindView(R.id.trans_bg) View mBgView;
    @BindView(R.id.pic_title_text) TextView mTitleView;
    @BindView(R.id.pic_image) ImageView mImageView;
    @BindView(R.id.pic_comments) TextView mCommentsView;
    @BindView(R.id.pic_faves) TextView mFavesView;

    private PicItem mPicItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_item);

        mPicItem = (PicItem) getIntent().getExtras().getSerializable("item");

        mTitleView.setText(mPicItem.getTitle());
        mCommentsView.setText(mPicItem.getComments());
        mFavesView.setText(mPicItem.getFaves());
        Picasso.with(this).load(mPicItem.getPicUrl()).into(mImageView);
    }

    @OnClick(R.id.trans_bg)
    public void onBgClicked(){
        super.onBackPressed();
    }
}
