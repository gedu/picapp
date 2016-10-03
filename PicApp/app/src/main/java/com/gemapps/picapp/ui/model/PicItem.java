package com.gemapps.picapp.ui.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by edu on 10/2/16.
 */

public class PicItem {

    @SerializedName("title") private String mTitle;
    @SerializedName("ownername") private String mOwnerName;
    @SerializedName("url_n") private String mPicUrl;

    public PicItem(String title, String ownerName, String picUrl) {
        mTitle = title;
        mOwnerName = ownerName;
        mPicUrl = picUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getOwnerName() {
        return mOwnerName;
    }

    public String getPicUrl() {
        return mPicUrl;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setOwnerName(String ownerName) {
        mOwnerName = ownerName;
    }

    public void setPicUrl(String picUrl) {
        mPicUrl = picUrl;
    }
}
