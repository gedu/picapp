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
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CommentAdapter";

    public static final int HEADER_VIEW_TYPE = 0;
    public static final int COMMENT_VIEW_TYPE = 1;

    private final Context mContext;
    private List<CommentItem> items;
    private PicItem mPicItem;

    public CommentAdapter(List<CommentItem> items, PicItem picItem, Context context) {
        this.items = items;
        this.mPicItem = picItem;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {

        if(viewType == COMMENT_VIEW_TYPE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_list_item, parent, false);
            return new CommentViewHolder(v);
        }else{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.photo_item_header, parent, false);
            return new HeaderViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0){

            HeaderViewHolder hHolder = (HeaderViewHolder) holder;

            hHolder.mUsernameView.setText(mPicItem.getOwnerName());

            hHolder.mPicTakenDateView.setText(mPicItem.getPicDateTaken());
            hHolder.mTitleView.setText(mPicItem.getTitle());
            hHolder.mCommentView.setText(mPicItem.getComments());
            hHolder.mFavesView.setText(mPicItem.getFaves());
        }else{
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
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER_VIEW_TYPE : COMMENT_VIEW_TYPE;
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

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_icon_image) ImageView mIconView;
        @BindView(R.id.user_name_text) TextView mUsernameView;
        @BindView(R.id.pic_title_text) TextView mTitleView;
        @BindView(R.id.pic_comments) TextView mCommentView;
        @BindView(R.id.pic_faves) TextView mFavesView;
        @BindView(R.id.pic_date_taken) TextView mPicTakenDateView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}