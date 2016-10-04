package com.gemapps.picapp.ui.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gemapps.picapp.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by edu on 10/4/16.
 * Wrapper class to use Butter Knife and set the pic description
 */

public class PicDescription {

    @BindView(R.id.pic_title_text) TextView mTitleView;
    @BindView(R.id.pic_comments) TextView mCommentView;
    @BindView(R.id.pic_faves) TextView mFavesView;
    @BindView(R.id.pic_image) ImageView mImageView;

    public PicDescription(View view) {
        ButterKnife.bind(this, view);
    }

    public void setupDescription(Context context, PicItem picItem){
        mTitleView.setText(picItem.getTitle());
        mCommentView.setText(picItem.getComments());
        mFavesView.setText(picItem.getFaves());

        Picasso.with(context).load(picItem.getPicUrl()).into(mImageView);
    }
}
