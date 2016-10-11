package com.gemapps.picapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.helper.CircleTransform;
import com.gemapps.picapp.ui.model.CommentItem;
import com.gemapps.picapp.ui.model.PicItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by edu on 10/7/16.
 */
public class CommentAdapter extends BaseCommentAdapter {

    private static final String TAG = "CommentAdapter";

    public CommentAdapter(List<CommentItem> items, PicItem picItem, Context context) {
        super(items, picItem, context);
    }

    @Override
    protected RecyclerView.ViewHolder getCommentViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_item, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    protected void bindCommentViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentItem item = items.get(position);
        CommentViewHolder cHolder = (CommentViewHolder) holder;

        cHolder.mAuthorName.setText(item.getAuthorName());
        cHolder.mTime.setText(item.getDateCreated() == null ? "" :
                DateUtils.getRelativeTimeSpanString(item.getPicDate().getTime(),
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        cHolder.mMsg.setText(item.getMsg());

        Picasso.with(mContext)
                .load(item.getIconUrl())
                .error(R.drawable.ic_buddy)
                .transform(new CircleTransform())
                .into(cHolder.mUserIcon);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_icon_image) ImageView mUserIcon;
        @BindView(R.id.author_name) TextView mAuthorName;
        @BindView(R.id.comment_time) TextView mTime;
        @BindView(R.id.comment_text) TextView mMsg;

        public CommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}