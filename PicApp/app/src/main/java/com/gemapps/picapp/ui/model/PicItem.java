package com.gemapps.picapp.ui.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by edu on 10/2/16.
 */

public class PicItem implements Serializable {

    @SerializedName("title") private String mTitle;
    @SerializedName("ownername") private String mOwnerName;
    @SerializedName("url_n") private String mPicUrl;
    @SerializedName("count_faves") private String mFaves;
    @SerializedName("count_comments") private String mComments;

    public PicItem(String title, String ownerName, String picUrl, String faves, String comments) {
        mTitle = title;
        mOwnerName = ownerName;
        mPicUrl = picUrl;
        mFaves = faves;
        mComments = comments;
    }

    public String getFaves() {
        return mFaves;
    }

    public void setFaves(String faves) {
        mFaves = faves;
    }

    public String getComments() {
        return mComments;
    }

    public void setComments(String comments) {
        mComments = comments;
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
