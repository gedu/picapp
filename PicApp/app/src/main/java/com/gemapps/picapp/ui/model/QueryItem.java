package com.gemapps.picapp.ui.model;

import android.view.View;
import android.widget.TextView;

import com.gemapps.picapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by edu on 10/3/16.
 * Wrapper class for the ViewStub to make it work with Butter knife
 */
public class QueryItem {

    public interface ClearListener {
        void onClear();
        void onUpdateQuery(String query);
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

    @OnClick(R.id.query_text)
    public void onQueryClicked(){

        mListener.onUpdateQuery(mQueryView.getText().toString());
    }
}
