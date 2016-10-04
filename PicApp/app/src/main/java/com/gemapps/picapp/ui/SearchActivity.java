package com.gemapps.picapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.gemapps.picapp.R;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseCardActivity {

    public static final String QUERY = "query";
    public static final String QUERY_BUNDLE = "query_bundle";

    @BindView(R.id.search_title_edit) TextInputEditText mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String query = getIntent().getExtras().getString(QUERY_BUNDLE);

        mInput.setText(query);

        mInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN){
                    onPositiveClicked();
                    return true;
                }

                return false;
            }
        });

        setResult(RESULT_CANCELED);
    }

    @OnClick(R.id.negative_button)
    public void onNegativeClicked(){
        super.onBackPressed();
    }

    @OnClick(R.id.positive_button)
    public void onPositiveClicked(){

        String txt = mInput.getText().toString();

        if (txt.length() == 0){
            mInput.setError(getString(R.string.empty_text_error));
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(QUERY, txt);

        setResult(RESULT_OK, intent);
        finish();
    }
}
