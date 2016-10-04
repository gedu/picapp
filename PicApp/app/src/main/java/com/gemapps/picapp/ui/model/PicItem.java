package com.gemapps.picapp.ui.model;

import com.gemapps.picapp.networking.FlickrClient;
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

    @SerializedName("id") private String mPicId;
    @SerializedName("farm") private String mFarm;
    @SerializedName("server") private String mServerId;
    @SerializedName("secret") private String mSecretId;

    public PicItem() {}

    public PicItem(String title, String ownerName, String picUrl, String faves, String comments, String picId, String farm, String serverId, String secretId) {
        mTitle = title;
        mOwnerName = ownerName;
        mPicUrl = picUrl;
        mFaves = faves;
        mComments = comments;
        mPicId = picId;
        mFarm = farm;
        mServerId = serverId;
        mSecretId = secretId;
    }

    public String getBigPicUrl(){
        return FlickrClient.buildPhotoUrl(mFarm, mServerId, mPicId, mSecretId, "c");
    }

    public String getPicId() {
        return mPicId;
    }

    public void setPicId(String picId) {
        mPicId = picId;
    }

    public String getFarm() {
        return mFarm;
    }

    public void setFarm(String farm) {
        mFarm = farm;
    }

    public String getServerId() {
        return mServerId;
    }

    public void setServerId(String serverId) {
        mServerId = serverId;
    }

    public String getSecretId() {
        return mSecretId;
    }

    public void setSecretId(String secretId) {
        mSecretId = secretId;
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
