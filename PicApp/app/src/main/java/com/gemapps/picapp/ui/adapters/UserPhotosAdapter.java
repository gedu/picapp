package com.gemapps.picapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gemapps.picapp.R;
import com.gemapps.picapp.ui.model.UserPhotoItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by edu on 10/21/16.
 */
public class UserPhotosAdapter extends RecyclerView.Adapter<UserPhotosAdapter.UserPhotoViewHolder> {
    private final Context mContext;
    private List<UserPhotoItem> items;

    public UserPhotosAdapter(List<UserPhotoItem> items, Context context) {
        this.items = items;
        this.mContext = context;
    }

    @Override
    public UserPhotoViewHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_photo_item_list, parent, false);
        return new UserPhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserPhotoViewHolder holder, int position) {
        UserPhotoItem item = items.get(position);

        Picasso.with(mContext)
                .load(item.getPhotoUrl())
                .placeholder(R.color.image_bg_holder)
                .into(holder.mUserPhoto);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public class UserPhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_photo) ImageView mUserPhoto;

        public UserPhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}