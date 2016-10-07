package com.gemapps.picapp.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.data.PicSqlHelper;
import com.gemapps.picapp.data.PicappContract;
import com.gemapps.picapp.helper.CircleTransform;
import com.gemapps.picapp.networking.FlickerUserClient;
import com.gemapps.picapp.ui.model.PicItem;
import com.gemapps.picapp.ui.model.UserItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class PhotoItemActivity extends BaseActivity {

    private static final String TAG = "PhotoItemActivity";
    public static final String ITEM_EXTRA_KEY = "picapp_item";

    @BindView(R.id.activity_photo_item) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.user_icon_image) ImageView mIconView;
    @BindView(R.id.user_name_text) TextView mUsernameView;
    @BindView(R.id.pic_title_text) TextView mTitleView;
    @BindView(R.id.pic_comments) TextView mCommentView;
    @BindView(R.id.pic_faves) TextView mFavesView;
    @BindView(R.id.pic_image) ImageView mImageView;
    @BindView(R.id.pic_date_taken) TextView mPicTakenDateView;

    private PicItem mPicItem;
    private UserItem mUserItem;

    private MenuItem mBookmarkItem;

    private boolean mInBookmark = false;

    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_item);

        setUpButtonToolbar();

        mPicItem = getIntent().getExtras().getParcelable(ITEM_EXTRA_KEY);
        new FlickerUserClient().getUserInfo(mPicItem.getOwnerId(), new FlickerUserClient.UserListener() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(UserItem userItem) {

                mUserItem = userItem;
                mBookmarkItem.setVisible(true);
                mInBookmark = existInDb();
                if(mBookmarkItem != null){
                    updateBookmarkState();
                }

                Picasso.with(PhotoItemActivity.this)
                        .load(userItem.getIconUrl())
                        .placeholder(R.drawable.ic_buddy)
                        .error(R.drawable.ic_buddy)
                        .transform(new CircleTransform()).into(mIconView);
            }
        });

        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        mUsernameView.setText(mPicItem.getOwnerName());
        mPicTakenDateView.setText(mPicItem.getPicDateTaken());
        mTitleView.setText(mPicItem.getTitle());
        mCommentView.setText(mPicItem.getComments());
        mFavesView.setText(mPicItem.getFaves());

        Picasso.with(this).load(mPicItem.getPicUrl()).into(mImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_photo_item, menu);
        mBookmarkItem = menu.getItem(0);
        mBookmarkItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_bookmark:

                if (mInBookmark){
                    removePhotoFromBookmark();
                }else{
                    addPhotoInBookmark();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateBookmarkState(){
        mBookmarkItem.setIcon(getResources().getDrawable(mInBookmark ?
                R.drawable.ic_bookmark_white_24px : R.drawable.ic_bookmark_border_white_24px));
    }

    private void removePhotoFromBookmark(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                PicSqlHelper helper = new PicSqlHelper(PhotoItemActivity.this);
                SQLiteDatabase removeDb = helper.getWritableDatabase();

                int count = removeDb.delete(PicappContract.BookmarkEntry.TABLE_NAME,
                        PicappContract.BookmarkEntry.COLUMN_UNIQUE_ID + "= ?",
                        new String[]{PicappContract.BookmarkEntry.buildUserUniqueId(mUserItem)});

                if(count > 0){
                    mInBookmark = false;
                    updateBookmarkState();
                    Snackbar.make(mCoordinatorLayout, R.string.deleted, Snackbar.LENGTH_SHORT).show();
                }
                removeDb.close();
            }
        });
    }

    private void addPhotoInBookmark(){

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                PicSqlHelper helper = new PicSqlHelper(PhotoItemActivity.this);
                SQLiteDatabase insertDb = helper.getWritableDatabase();

                Log.d(TAG, "run: USER ITEM: "+mUserItem);
                ContentValues contentValues = PicappContract.BookmarkEntry.parse(mPicItem, mUserItem);

                long id = insertDb.insert(PicappContract.BookmarkEntry.TABLE_NAME, null, contentValues);

                if(id != -1){
                    mInBookmark = true;
                    Snackbar.make(mCoordinatorLayout, R.string.saved, Snackbar.LENGTH_SHORT).show();
                }else{
                    mInBookmark = false;
                    Snackbar.make(mCoordinatorLayout, R.string.saved_error_text, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.try_again, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addPhotoInBookmark();
                                }
                            }).show();
                }
                updateBookmarkState();
                insertDb.close();
            }
        });
    }

    private boolean existInDb(){
        PicSqlHelper helper = new PicSqlHelper(PhotoItemActivity.this);
        SQLiteDatabase readDb = helper.getReadableDatabase();

        Cursor cursor = readDb.query(PicappContract.BookmarkEntry.TABLE_NAME,
                null,
                PicappContract.BookmarkEntry.COLUMN_UNIQUE_ID + "= ?",
                new String[]{PicappContract.BookmarkEntry.buildUserUniqueId(mUserItem)},
                null, null, null);

        boolean exist = cursor != null && cursor.getCount() > 0;

        if(cursor != null) cursor.close();
        readDb.close();
        return exist;
    }
}
