package com.gemapps.picapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;

import com.gemapps.picapp.R;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseCardActivity {

    public static final String QUERY = "query";

    @BindView(R.id.search_title_edit) TextInputEditText mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
