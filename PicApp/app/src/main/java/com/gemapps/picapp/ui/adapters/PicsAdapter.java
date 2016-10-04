package com.gemapps.picapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.ui.model.PicItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by edu on 10/2/16.
 */
public class PicsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "PicsAdapter";

    public interface OnItemClickListener {
        void onClick(PicItem item, View title, View image);
    }

    public static final int VIEW_LOADING_TYPE = 0;
    public static final int VIEW_ITEM_TYPE = 1;

    private final Context context;
    private List<PicItem> mItems;

    private int mGridHeight;
    private int mLinearHeight;

    private int mCurrentHeight;
    private OnItemClickListener mListener;

    public PicsAdapter(List<PicItem> items, Context context) {
        this.mItems = items;
        this.context = context;

        mLinearHeight = (int) context.getResources().getDimension(R.dimen.pic_item_linear_list_height);
        mGridHeight = (int) context.getResources().getDimension(R.dimen.pic_item_grid_list_height);

        mCurrentHeight = mLinearHeight;
    }

    public void setListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                            int viewType) {

        if(viewType == VIEW_ITEM_TYPE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pic_item_list_view, parent, false);
            return new PicViewHolder(v);
        }else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bottom_progress_item_list, parent, false);
            return new LoadingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PicItem item = mItems.get(position);

        if(holder instanceof PicViewHolder) {

            final PicViewHolder picHolder = (PicViewHolder)holder;
            picHolder.mTitle.setText(item.getTitle());
            picHolder.mOwnerName.setText(context.getString(R.string.owner_name, item.getOwnerName()));
            Picasso.with(context)
                    .load(item.getPicUrl())
                    .placeholder(R.color.image_bg_holder)
                    .into(picHolder.mImage);

            final ViewGroup.LayoutParams imageParam = picHolder.mImage.getLayoutParams();
            imageParam.height = mCurrentHeight;
            picHolder.mImage.setLayoutParams(imageParam);

            picHolder.mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mListener != null) mListener.onClick(item, picHolder.mTitle, picHolder.mImage);
                }
            });
        }
    }

    public void addContent(List<PicItem> moreContent){
        int insertedPos = getItemCount();
        mItems.addAll(moreContent);
        notifyItemRangeInserted(insertedPos, moreContent.size());
    }

    public void updateImageHeight(boolean isLinear){
        mCurrentHeight = isLinear ? mLinearHeight : mGridHeight;
    }

    public void addProgressItem(){
        mItems.add(null);
        notifyItemInserted(mItems.size());
    }

    public void removeProgressItem(){
        mItems.remove(null);
        notifyItemRemoved(mItems.size() + 1);
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
        return mItems.get(position) == null ? VIEW_LOADING_TYPE : VIEW_ITEM_TYPE;
    }

    public class PicViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pic_container) View mContainer;
        @BindView(R.id.pic_title_text) TextView mTitle;
        @BindView(R.id.pic_owner_name_text) TextView mOwnerName;
        @BindView(R.id.pic_image) ImageView mImage;

        public PicViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class LoadingHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progressBar) ProgressBar mProgressBar;

        public LoadingHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}