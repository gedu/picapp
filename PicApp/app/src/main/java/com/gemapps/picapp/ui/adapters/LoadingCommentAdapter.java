package com.gemapps.picapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gemapps.picapp.R;
import com.gemapps.picapp.ui.model.CommentItem;
import com.gemapps.picapp.ui.model.PicItem;

import java.util.List;

/**
 * Created by edu on 10/11/16.
 *
 * Will show a item with the progress circle bar
 */
public class LoadingCommentAdapter extends BaseCommentAdapter {
    private static final String TAG = "LoadingCommentAdapter";
    public LoadingCommentAdapter(List<CommentItem> items, PicItem picItem, Context context) {
        super(items, picItem, context);
        items.add(new CommentItem());
    }

    @Override
    protected RecyclerView.ViewHolder getCommentViewHolder(ViewGroup parent, int viewType) {

        return new LoadingViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.loading, parent, false));
    }

    @Override
    protected void bindCommentViewHolder(RecyclerView.ViewHolder holder, int position) {}


    private class LoadingViewHolder extends RecyclerView.ViewHolder{

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
