package com.gemapps.picapp.ui.adapters;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gemapps.picapp.R;
import com.gemapps.picapp.data.PicappContract;
import com.gemapps.picapp.helper.CircleTransform;
import com.gemapps.picapp.ui.model.PicItem;
import com.gemapps.picapp.ui.model.UserItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by edu on 10/5/16.
 */

public class BookmarkCursorLoader extends CursorAdapter {

    private static final String TAG = "BookmarkCursorLoader";

    public interface BookmarkListener {
        void onClicked(PicItem picItem, View imageView);
    }

    private BookmarkListener mListener;

    public BookmarkCursorLoader(Context context, BookmarkListener listener) {
        super(context, null, 0);
        mListener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(mContext).inflate(R.layout.bookmark_item_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final BookmarkViewHolder holder = new BookmarkViewHolder(view);
        final UserItem userItem = new UserItem(getCursor());
        final PicItem picItem = new PicItem(getCursor());
        picItem.setUserItem(userItem);

        holder.mPicTitle.setText(cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_TITLE)));
        holder.mUserName.setText(cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_OWNER_NAME)));
//
        String picUrl = cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_URL_N));
//        String iconFarm = cursor.getString(cursor.getColumnIndex(PicappContract.BookmarkEntry.COLUMN_ICON_FARM_ID));
//        String iconServer = cursor.getString(cursor.getColumnIndex(PicappContract.BookmarkEntry.COLUMN_ICON_SERVER_ID));
//        String nsid = cursor.getString(cursor.getColumnIndex(PicappContract.BookmarkEntry.COLUMN_OWNER_NSID));
//
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onClicked(picItem, holder.mIconImage);
            }
        });
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
        @BindView(R.id.bookmark_container) View mContainer;

        public BookmarkViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.bookmark_container)
        public void onItemClicked(View view){

            Toast.makeText(mContext, "YAY", Toast.LENGTH_SHORT).show();
        }
    }
}
