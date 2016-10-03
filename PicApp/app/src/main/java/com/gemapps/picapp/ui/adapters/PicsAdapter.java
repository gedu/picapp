package com.gemapps.picapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class PicsAdapter extends RecyclerView.Adapter<PicsAdapter.PicViewHolder> {

    public interface OnItemClickListener {
        void onClick(PicItem item, View title, View image);
    }

    private final Context context;
    private List<PicItem> items;

    private int mGridHeight;
    private int mLinearHeight;

    private int mCurrentHeight;
    private OnItemClickListener mListener;

    public PicsAdapter(List<PicItem> items, Context context) {
        this.items = items;
        this.context = context;

        mLinearHeight = (int) context.getResources().getDimension(R.dimen.pic_item_linear_list_height);
        mGridHeight = (int) context.getResources().getDimension(R.dimen.pic_item_grid_list_height);

        mCurrentHeight = mLinearHeight;
    }

    public void setListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public PicViewHolder onCreateViewHolder(ViewGroup parent,
                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pic_item_list_view, parent, false);
        return new PicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PicViewHolder holder, int position) {
        final PicItem item = items.get(position);

        holder.mTitle.setText(item.getTitle());
        holder.mOwnerName.setText(context.getString(R.string.owner_name, item.getOwnerName()));
        Picasso.with(context)
                .load(item.getPicUrl())
                .placeholder(R.color.image_bg_holder)
                .into(holder.mImage);

        final ViewGroup.LayoutParams imageParam = holder.mImage.getLayoutParams();
        imageParam.height = mCurrentHeight;
        holder.mImage.setLayoutParams(imageParam);

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null) mListener.onClick(item, holder.mTitle, holder.mImage);
            }
        });

    }

    public void updateImageHeigth(boolean isLinear){
        mCurrentHeight = isLinear ? mLinearHeight : mGridHeight;
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
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
}