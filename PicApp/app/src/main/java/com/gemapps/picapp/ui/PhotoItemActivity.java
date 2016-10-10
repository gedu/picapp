package com.gemapps.picapp.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.data.PicSqlHelper;
import com.gemapps.picapp.data.PicappContract;
import com.gemapps.picapp.networking.FlickerUserClient;
import com.gemapps.picapp.networking.FlickrCommentsClient;
import com.gemapps.picapp.ui.adapters.CommentAdapter;
import com.gemapps.picapp.ui.model.CommentItem;
import com.gemapps.picapp.ui.model.PicItem;
import com.gemapps.picapp.ui.model.UserItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;

public class PhotoItemActivity extends BaseActivity {

    private static final String TAG = "PhotoItemActivity";
    public static final String ITEM_EXTRA_KEY = "picapp_item";

    @BindView(R.id.activity_photo_item) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @BindView(R.id.pic_image) ImageView mImageView;

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(getLoadingAdapter());
        mRecyclerView.setAdapter(new CommentAdapter(Collections.singletonList(new CommentItem()), mPicItem, this));

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

//                Picasso.with(PhotoItemActivity.this)
//                        .load(userItem.getIconUrl())
//                        .placeholder(R.drawable.ic_buddy)
//                        .error(R.drawable.ic_buddy)
//                        .transform(new CircleTransform()).into(mIconView);
            }
        });

        new FlickrCommentsClient().getComments(mPicItem.getPicId(), new FlickrCommentsClient.CommentsListener() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(ArrayList<CommentItem> comments) {

                comments.add(0, new CommentItem());
                mRecyclerView.setAdapter(new CommentAdapter(comments, mPicItem, PhotoItemActivity.this));
            }
        });

        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

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

    private RecyclerView.Adapter<RecyclerView.ViewHolder> getLoadingAdapter(){
        return new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new LoadingViewHolder(PhotoItemActivity.this.getLayoutInflater()
                        .inflate(R.layout.loading, parent, false));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}

            @Override
            public int getItemCount() {
                return 0;
            }

            class LoadingViewHolder extends RecyclerView.ViewHolder{

                public LoadingViewHolder(View itemView) {
                    super(itemView);
                }
            }
        };
    }//
}
