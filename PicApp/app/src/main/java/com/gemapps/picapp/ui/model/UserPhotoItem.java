package com.gemapps.picapp.ui.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by edu on 10/20/16.
 */

public class UserPhotoItem {

    /**
     * farm
     * server
     * id
     * secret
     */
    private static final String PHOTO_URL = "https://farm%s.staticflickr.com/%s/%s_%s.jpg";

    @SerializedName("id") private String mId;
    @SerializedName("owner") private String mOwnerId;
    @SerializedName("secret") private String mSecretId;
    @SerializedName("server") private String mServerId;
    @SerializedName("farm") private String mFarmId;
    @SerializedName("title") private String mTitle;

    public UserPhotoItem(String id, String ownerId, String secretId, String serverId, String farmId, String title) {
        mId = id;
        mOwnerId = ownerId;
        mSecretId = secretId;
        mServerId = serverId;
        mFarmId = farmId;
        mTitle = title;
    }

    public String getPhotoUrl(){
        return String.format(PHOTO_URL, mFarmId, mServerId, mId, mSecretId);
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(String ownerId) {
        mOwnerId = ownerId;
    }

    public String getSecretId() {
        return mSecretId;
    }

    public void setSecretId(String secretId) {
        mSecretId = secretId;
    }

    public String getServerId() {
        return mServerId;
    }

    public void setServerId(String serverId) {
        mServerId = serverId;
    }

    public String getFarmId() {
        return mFarmId;
    }

    public void setFarmId(String farmId) {
        mFarmId = farmId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
