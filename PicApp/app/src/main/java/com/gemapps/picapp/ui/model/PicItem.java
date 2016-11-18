package com.gemapps.picapp.ui.model;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.gemapps.picapp.data.PicappContract;
import com.gemapps.picapp.networking.FlickrPhotosClient;
import com.gemapps.picapp.networking.FlickrUserClient;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.gemapps.picapp.ui.PhotoItemActivity.USER_EXTRA_KEY;
import static com.gemapps.picapp.ui.model.PicItem.PicMessage.LOAD_SUCCESS;

/**
 * Created by edu on 10/2/16.
 * Photo model to be used by Gson
 */
public class PicItem implements Parcelable {

    private static final String TAG = "PicItem";
    private static final SimpleDateFormat PIC_FORMAT = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    private static final SimpleDateFormat PIC_HEADER_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @SerializedName("title") private String mTitle;
    @SerializedName("owner") private String mOwnerId;
    @SerializedName("ownername") private String mOwnerName;
    @SerializedName("url_n") private String mPicUrl;
    @SerializedName("count_faves") private String mFaves;
    @SerializedName("count_comments") private String mComments;
    @SerializedName("datetaken") private String mPicDateTaken;

    @SerializedName("id") private String mPicId;
    @SerializedName("farm") private String mFarm;
    @SerializedName("server") private String mServerId;
    @SerializedName("secret") private String mSecretId;

    private transient UserItem mUserItem;

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
        mUserItem = in.readParcelable(UserItem.class.getClassLoader());
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

    public PicItem(Cursor cursor){

        mTitle = cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_TITLE));
        mOwnerId = cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_OWNER_ID));
        mOwnerName = cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_OWNER_NAME));
        mPicUrl = cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_URL_N));
        mFaves = cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_COUNT_FAVES));
        mComments = cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_COUNT_COMMENTS));
        mPicDateTaken = cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_DATE_TAKEN));
        mPicId = cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_PIC_ID));
        mFarm = cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_FARM_ID));
        mServerId = cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_SERVER_ID));
        mSecretId = cursor.getString(cursor.getColumnIndex(PicappContract.PublicationEntry.COLUMN_SECRET_ID));
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

    public String getPicDateFormatted() {

        try {
            Date fullDate = PIC_FORMAT.parse(mPicDateTaken);
            return PIC_HEADER_FORMAT.format(fullDate);
        } catch (ParseException e) {
            return "";
        }
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

    /**
     * @return the user obj or null if the loading didn't finish
     */
    public UserItem getUserItem() {
        return mUserItem;
    }

    public void setUserItem(UserItem userItem){
        mUserItem = userItem;
    }

    public void setUserItem() {

        new FlickrUserClient().getUserInfo(mOwnerId, new FlickrUserClient.UserListener() {
            @Override
            public void onFailure() {}

            @Override
            public void onSuccess(UserItem userItem) {
                mUserItem = userItem;

                if(EventBus.getDefault().hasSubscriberForEvent(PicMessage.class)) {
                    PicMessage message = new PicMessage(LOAD_SUCCESS);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(USER_EXTRA_KEY, userItem);
                    message.setBundle(bundle);

                    EventBus.getDefault().post(message);
                }
            }
        });
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
        dest.writeParcelable(mUserItem, flags);
    }

    public static final Parcelable.Creator<PicItem> CREATOR = new Parcelable.Creator<PicItem>() {
        public PicItem createFromParcel(Parcel in) {
            return new PicItem(in);
        }

        public PicItem[] newArray(int size) {
            return new PicItem[size];
        }
    };

    public static class PicMessage {

        public static int LOAD_SUCCESS = 1;

        private int mMode;
        private Bundle mBundle;

        public PicMessage(int mode) {
            mMode = mode;
        }

        public int getMode() {
            return mMode;
        }

        public Bundle getBundle() {
            return mBundle;
        }

        public void setBundle(Bundle bundle) {
            mBundle = bundle;
        }
    }
}
