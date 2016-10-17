package com.gemapps.picapp.ui.adapters;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.data.PicappContract;
import com.gemapps.picapp.helper.CircleTransform;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by edu on 10/5/16.
 */

public class BookmarkCursorLoader extends CursorAdapter {

    private static final String TAG = "BookmarkCursorLoader";
    public BookmarkCursorLoader(Context context) {
        super(context, null, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(mContext).inflate(R.layout.bookmark_item_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        BookmarkViewHolder holder = new BookmarkViewHolder(view);

        holder.mPicTitle.setText(cursor.getString(cursor.getColumnIndex(PicappContract.BookmarkEntry.COLUMN_PIC_TITLE)));
        holder.mUserName.setText(cursor.getString(cursor.getColumnIndex(PicappContract.BookmarkEntry.COLUMN_OWNER_NAME)));

        String picUrl = cursor.getString(cursor.getColumnIndex(PicappContract.BookmarkEntry.COLUMN_PIC_URL));
//        String iconFarm = cursor.getString(cursor.getColumnIndex(PicappContract.BookmarkEntry.COLUMN_ICON_FARM_ID));
//        String iconServer = cursor.getString(cursor.getColumnIndex(PicappContract.BookmarkEntry.COLUMN_ICON_SERVER_ID));
//        String nsid = cursor.getString(cursor.getColumnIndex(PicappContract.BookmarkEntry.COLUMN_OWNER_NSID));

        Picasso.with(context)
                .load(picUrl)
                .placeholder(R.drawable.ic_buddy)
                .transform(new CircleTransform())
                .into(holder.mIconImage);
    }

    public class BookmarkViewHolder {

        @BindView(R.id.pic_image) ImageView mIconImage;
        @BindView(R.id.pic_title_text) TextView mPicTitle;
        @BindView(R.id.user_name_text) TextView mUserName;

        public BookmarkViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.bookmark_container)
        public void onItemClicked(View view){

        }
    }
}
