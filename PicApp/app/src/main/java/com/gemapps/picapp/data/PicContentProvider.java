package com.gemapps.picapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by edu on 10/5/16.
 */

public class PicContentProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher mUriMatcher = buildUriMatcher();
    private PicSqlHelper mOpenHelper;

    private static final int BOOKMARK = 1;

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(PicappContract.CONTENT_AUTHORITY, PicappContract.PATH_BOOKMARK, BOOKMARK);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new PicSqlHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;
        switch (mUriMatcher.match(uri)){
            case BOOKMARK:

                cursor = mOpenHelper.getReadableDatabase()
                        .query(PicappContract.BookmarkEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);

        switch (match){
            case BOOKMARK: return PicappContract.BookmarkEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    //No using them right now
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
