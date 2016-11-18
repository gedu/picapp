package com.gemapps.picapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.networking.FlickrPhotoInfoClient;
import com.gemapps.picapp.ui.model.PhotoInfoItem;
import com.gemapps.picapp.ui.model.UserPhotoItem;
import com.gemapps.picapp.ui.widget.FixScaleImageVIew;
import com.gemapps.picapp.ui.widget.TranslationTextView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

public class PhotoDetailActivity extends BaseActivity
        implements FlickrPhotoInfoClient.PhotoInfoListener {

    private static final String TAG = "PhotoDetailActivity";

    private static final String PHOTO_ITEM_PREF = "picapp.photo_item_pref";
    private static final String AUTHOR_NAME_PREF = "picapp.author_name_pref";

    @BindView(R.id.author_name) TranslationTextView mAuthorName;
    @BindView(R.id.photo_title) TextView mPhotoTitleView;
    @BindView(R.id.photo_description) TextView mPhotoDescription;
    @BindView(R.id.bottom_sheet) View mBottomDescription;
    @BindView(R.id.photo_image) FixScaleImageVIew mPhoto;

    private BottomSheetBehavior mSheetBehavior;

    public static Intent getInstance(Context context, UserPhotoItem userPhotoItem, String authorName){

        Intent intent = new Intent(context, PhotoDetailActivity.class);

//        intent.setExtrasClassLoader(UserPhotoItem.class.getClassLoader()); //for amazon kindle fires
        intent.putExtra(PHOTO_ITEM_PREF, userPhotoItem);
        intent.putExtra(AUTHOR_NAME_PREF, authorName);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        setUpButtonToolbar();
        setToolbarTitle("");

        mSheetBehavior = BottomSheetBehavior.from(mBottomDescription);
        mSheetBehavior.setPeekHeight(200);
        mSheetBehavior.setState(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        UserPhotoItem item = getIntent().getExtras().getParcelable(PHOTO_ITEM_PREF);
        String authorName = getIntent().getExtras().getString(AUTHOR_NAME_PREF);

        new FlickrPhotoInfoClient().getPhotoInfo(item.getId(), this);

        mAuthorName.setText(authorName);

        Picasso.with(this).load(item.getPhotoUrl()).into(mPhoto);
    }

    @OnClick(R.id.bottom_sheet)
    public void onSheetClicked(){

        if((mSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)){

            mAuthorName.translateForward();
            mPhoto.zoomIn();
            mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else{

            mAuthorName.translateBack();
            mPhoto.zoomOut();
            mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onSuccess(PhotoInfoItem photoItem) {

        mPhotoTitleView.setText(photoItem.getTitleContent());
        mPhotoDescription.setText(photoItem.getDescriptionContent().length() > 0 ?
                photoItem.getDescriptionContent() : getString(R.string.no_description_text));

    }
}
