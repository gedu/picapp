package com.gemapps.picapp.ui;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.MenuItem;
import android.widget.ListView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.data.PicappContract;
import com.gemapps.picapp.ui.adapters.BookmarkCursorLoader;

import butterknife.BindView;

public class BookmarkActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "BookmarkActivity";
    @BindView(R.id.list_view) ListView mListView;

    private BookmarkCursorLoader mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bookmark);

        mAdapter = new BookmarkCursorLoader(this);
        mListView.setAdapter(mAdapter);

        setUpButtonToolbar();

        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, PicappContract.BookmarkEntry.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mAdapter.swapCursor(null);
    }
}
