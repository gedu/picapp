package com.gemapps.picapp.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
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
import butterknife.OnClick;

/**
 * Created by edu on 10/11/16.
 * Main class to add a comment header the comments list
 */
public abstract class BaseCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "BaseCommentAdapter";

    public interface BaseCommentListener {
        void onPlayerClicked(ImageView imageView);
    }

    public static final int HEADER_VIEW_TYPE = 0;
    public static final int COMMENT_VIEW_TYPE = 1;

    protected final Context mContext;
    protected List<CommentItem> mItems;
    private PicItem mPicItem;

    private UserItem mUserItem;
    private HeaderViewHolder mHeaderHolder;
    private BaseCommentListener mListener;

    private Resources mRes;

    private boolean mWithHeader = true;

    public BaseCommentAdapter(List<CommentItem> items, PicItem picItem, Context context) {
        this.mItems = items;
        this.mPicItem = picItem;
        this.mContext = context;
        this.mRes = context.getResources();

        //Adding the header element
        this.mItems.add(0, new CommentItem());
    }

    public void removeHeader(){
        mWithHeader = false;
        mItems.remove(0);
    }

    public void setListener(BaseCommentListener listener){
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == HEADER_VIEW_TYPE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.photo_item_header, parent, false);
            return new CommentAdapter.HeaderViewHolder(v);

        }else {
            return getCommentViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0 && mWithHeader){

            mHeaderHolder = (HeaderViewHolder) holder;

            final int commentCount = mPicItem.getCommentAmount();
            final int favesCount = mPicItem.getFavesAmount();

            mHeaderHolder.mUsernameView.setText(mPicItem.getOwnerName());
            mHeaderHolder.mPicTakenDateView.setText(mPicItem.getPicDateFormatted());
            mHeaderHolder.mTitleView.setText(mPicItem.getTitle());
            mHeaderHolder.mCommentView.setText(mRes.getQuantityString(R.plurals.comments,
                    commentCount, commentCount));
            mHeaderHolder.mFavesView.setText(mRes.getQuantityString(R.plurals.faves, favesCount,
                    favesCount));

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
        if (mItems == null) {
            return 0;
        }
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 && mWithHeader ? HEADER_VIEW_TYPE : COMMENT_VIEW_TYPE;
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

        @OnClick(R.id.user_icon_image)
        public void onPlayerClicked(ImageView view){

            if (mListener != null)mListener.onPlayerClicked(view);
        }
    }
}
