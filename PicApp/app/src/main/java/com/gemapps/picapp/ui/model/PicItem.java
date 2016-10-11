package com.gemapps.picapp.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gemapps.picapp.networking.FlickrPhotosClient;
import com.google.gson.annotations.SerializedName;

/**
 * Created by edu on 10/2/16.
 * Photo model to be used by Gson
 */
public class PicItem implements Parcelable {

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

    public PicItem(Parcel in) {
        mTitle = in.readString();
        mOwnerId = in.readString();
        mOwnerName = in.readString();
        mPicUrl = in.readString();
        mFaves = in.readString();
        mComments = in.readString();
        mPicDateTaken = in.readString();
        mPicId = in.readString();
        mFarm = in.readString();
        mServerId = in.readString();
        mSecretId = in.readString();
    }

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

    public int getFavesAmount(){
        return Integer.parseInt(mFaves);
    }

    public void setFaves(String faves) {
        mFaves = faves;
    }

    public String getComments() {
        return mComments;
    }

    public int getCommentAmount(){
        return Integer.parseInt(mComments);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mTitle);
        dest.writeString(mOwnerId);
        dest.writeString(mOwnerName);
        dest.writeString(mPicUrl);
        dest.writeString(mFaves);
        dest.writeString(mComments);
        dest.writeString(mPicDateTaken);
        dest.writeString(mPicId);
        dest.writeString(mFarm);
        dest.writeString(mServerId);
        dest.writeString(mSecretId);
    }

    public static final Parcelable.Creator<PicItem> CREATOR = new Parcelable.Creator<PicItem>() {
        public PicItem createFromParcel(Parcel in) {
            return new PicItem(in);
        }

        public PicItem[] newArray(int size) {
            return new PicItem[size];
        }
    };
}
