package com.gemapps.picapp.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.ui.model.PicDescription;
import com.gemapps.picapp.ui.model.PicItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class PhotoItemActivity extends BaseActivity {

    public static final String ITEM_EXTRA_KEY = "picapp_item";

    @BindView(R.id.nested_scroll) ListView mListView;

    @BindView(R.id.pic_image)
    ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_item);

        setUpButtonToolbar();

        View picDescriptionView = getLayoutInflater()
                .inflate(R.layout.photo_item_header, mListView, false);

        PicItem picItem = (PicItem) getIntent().getExtras().getSerializable(ITEM_EXTRA_KEY);

        new PicDescription(picDescriptionView).setupDescription(this, picItem);
        mListView.addHeaderView(picDescriptionView);
        mListView.setAdapter(null);
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
