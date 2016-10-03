package com.gemapps.picapp.ui.model;

import android.view.View;
import android.widget.TextView;

import com.gemapps.picapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by edu on 10/3/16.
 */

public class QueryItem {

    public interface ClearListener {
        void onClear();
    }

    @BindView(R.id.query_text) TextView mQueryView;

    private ClearListener mListener;

    public QueryItem(View view, String query, ClearListener listener) {
        ButterKnife.bind(this, view);
        mListener = listener;
        mQueryView.setText(query);
    }

    @OnClick(R.id.clear_button)
    public void onClearClicked(){

        mListener.onClear();
    }
}
