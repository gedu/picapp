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

    private final Context context;
    private List<PicItem> items;

    public PicsAdapter(List<PicItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public PicViewHolder onCreateViewHolder(ViewGroup parent,
                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pic_item_list_view, parent, false);
        return new PicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PicViewHolder holder, int position) {
        PicItem item = items.get(position);

        holder.mTitle.setText(item.getTitle());
        holder.mOwnerName.setText(item.getOwnerName());
        Picasso.with(context).load(item.getPicUrl()).into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public class PicViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pic_title_text) TextView mTitle;
        @BindView(R.id.pic_owner_name_text) TextView mOwnerName;
        @BindView(R.id.pic_image) ImageView mImage;

        public PicViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}