package com.gemapps.picapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.helper.CircleTransform;
import com.gemapps.picapp.ui.model.CommentItem;
import com.gemapps.picapp.ui.model.PicItem;
import com.gemapps.picapp.ui.model.UserItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by edu on 10/11/16.
 * Main class to add a comment header the comments list
 */
public abstract class BaseCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "BaseCommentAdapter";

    public static final int HEADER_VIEW_TYPE = 0;
    public static final int COMMENT_VIEW_TYPE = 1;

    protected final Context mContext;
    protected List<CommentItem> items;
    protected PicItem mPicItem;

    private UserItem mUserItem;

    private HeaderViewHolder mHeaderHolder;

    public BaseCommentAdapter(List<CommentItem> items, PicItem picItem, Context context) {
        this.items = items;
        this.mPicItem = picItem;
        this.mContext = context;

        //Adding the header element
        this.items.add(0, new CommentItem());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == COMMENT_VIEW_TYPE) {
            return getCommentViewHolder(parent, viewType);
        }else{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.photo_item_header, parent, false);
            return new CommentAdapter.HeaderViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0){

            mHeaderHolder = (HeaderViewHolder) holder;

            mHeaderHolder.mUsernameView.setText(mPicItem.getOwnerName());
            mHeaderHolder.mPicTakenDateView.setText(mPicItem.getPicDateTaken());
            mHeaderHolder.mTitleView.setText(mPicItem.getTitle());
            mHeaderHolder.mCommentView.setText(mPicItem.getComments());
            mHeaderHolder.mFavesView.setText(mPicItem.getFaves());

            if(mUserItem != null) {
                Picasso.with(mContext)
                        .load(mUserItem.getIconUrl())
                        .placeholder(R.drawable.ic_buddy)
                        .error(R.drawable.ic_buddy)
                        .transform(new CircleTransform()).into(mHeaderHolder.mIconView);
            }
        }else{

            bindCommentViewHolder(holder, position);
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

    public void setUserIcon(UserItem userItem){
        if(mHeaderHolder == null){
            mUserItem = userItem;
        }else {
            Picasso.with(mContext)
                    .load(userItem.getIconUrl())
                    .placeholder(R.drawable.ic_buddy)
                    .error(R.drawable.ic_buddy)
                    .transform(new CircleTransform()).into(mHeaderHolder.mIconView);
        }
    }

    protected abstract RecyclerView.ViewHolder getCommentViewHolder(ViewGroup parent, int viewType);

    protected abstract void bindCommentViewHolder(RecyclerView.ViewHolder holder, int position);

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_icon_image)
        ImageView mIconView;
        @BindView(R.id.user_name_text)
        TextView mUsernameView;
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
