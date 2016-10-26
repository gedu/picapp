package com.gemapps.picapp.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by edu on 10/26/16.
 */
public class PhotoInfoItem implements Parcelable {

    @SerializedName("id") private String mId;
    @SerializedName("secret") private String mSecretId;
    @SerializedName("server") private String mServerId;
    @SerializedName("farm") private String mFarmId;
    @SerializedName("description") private Description mDescription;
    @SerializedName("title") private Title mTitle;


    public PhotoInfoItem(String id, String secretId, String serverId, String farmId,
                         Description description, Title title) {
        mId = id;
        mSecretId = secretId;
        mServerId = serverId;
        mFarmId = farmId;
        mDescription = description;
        mTitle = title;
    }

    public PhotoInfoItem(Parcel in) {

        mId = in.readString();
        mSecretId = in.readString();
        mServerId = in.readString();
        mFarmId = in.readString();
        mDescription = (Description) in.readSerializable();
        mTitle = (Title) in.readSerializable();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
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

    public Description getDescription() {
        return mDescription;
    }

    public String getDescriptionContent(){
        return mDescription.getDescription();
    }

    public void setDescription(Description description) {
        mDescription = description;
    }

    public Title getTitle() {
        return mTitle;
    }

    public String getTitleContent(){
        return mTitle.getTitle();
    }

    public void setTitle(Title title) {
        mTitle = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mSecretId);
        dest.writeString(mServerId);
        dest.writeString(mFarmId);
        dest.writeSerializable(mDescription);
        dest.writeSerializable(mTitle);
    }

    public static final Creator<PhotoInfoItem> CREATOR = new Creator<PhotoInfoItem>() {
        @Override
        public PhotoInfoItem createFromParcel(Parcel in) {
            return new PhotoInfoItem(in);
        }

        @Override
        public PhotoInfoItem[] newArray(int size) {
            return new PhotoInfoItem[size];
        }
    };


    public class Description implements Serializable {
        @Expose
        @SerializedName("_content") private String mDescription;

        public Description() {}

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
        }
    }

    public class Title implements Serializable {

        @SerializedName("_content") private String mTitle;

        public Title() {}

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            mTitle = title;
        }
    }
}
