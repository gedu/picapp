package com.gemapps.picapp.ui.model;

import com.gemapps.picapp.networking.FlickrPhotosClient;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by edu on 10/2/16.
 * Photo model to be used by Gson
 */
public class PicItem implements Serializable {

    @SerializedName("title") private String mTitle;
    @SerializedName("owner") private String mOwnerId;
    @SerializedName("ownername") private String mOwnerName;
    @SerializedName("url_n") private String mPicUrl;
    @SerializedName("count_faves") private String mFaves;
    @SerializedName("count_comments") private String mComments;
    @SerializedName("date_taken") private String mPicDateTaken;

    @SerializedName("id") private String mPicId;
    @SerializedName("farm") private String mFarm;
    @SerializedName("server") private String mServerId;
    @SerializedName("secret") private String mSecretId;

    public PicItem() {}

    public PicItem(String title, String ownerId, String ownerName, String picUrl, String faves, String comments,
                   String picDateTaken, String picId, String farm, String serverId, String secretId) {
        mTitle = title;
        mOwnerId = ownerId;
        mOwnerName = ownerName;
        mPicUrl = picUrl;
        mFaves = faves;
        mComments = comments;
        mPicDateTaken = picDateTaken;
        mPicId = picId;
        mFarm = farm;
        mServerId = serverId;
        mSecretId = secretId;
    }

    public String getBigPicUrl(){
        return FlickrPhotosClient.buildPhotoUrl(mFarm, mServerId, mPicId, mSecretId, "c");
    }

    public String getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(String ownerId) {
        mOwnerId = ownerId;
    }

    public String getPicDateTaken() {
        return mPicDateTaken;
    }

    public void setPicDateTaken(String picDateTaken) {
        mPicDateTaken = picDateTaken;
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
