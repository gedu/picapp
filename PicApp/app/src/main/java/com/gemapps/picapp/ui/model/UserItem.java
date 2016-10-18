package com.gemapps.picapp.ui.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.gemapps.picapp.data.PicappContract;
import com.google.gson.annotations.SerializedName;

/**
 * Created by edu on 10/5/16.
 * User model to be used by Gson
 */
public class UserItem implements Parcelable {

    /**
     * 1 iconFarm
     * 2 iconServer
     * 3 nsid
     */
    public static final String USER_PIC_URL = "http://farm%s.staticflickr.com/%s/buddyicons/%s.jpg";


    @SerializedName("id") private String mId;
    @SerializedName("nsid") private String mNsid;
    @SerializedName("iconserver") private String mIconServerId;
    @SerializedName("iconfarm") private String mIconFarmId;

    public UserItem(Parcel in){
        mId = in.readString();
        mNsid = in.readString();
        mIconServerId = in.readString();
        mIconFarmId = in.readString();
    }

    public UserItem(String id, String nsid, String iconServerId, String iconFarmId) {
        mId = id;
        mNsid = nsid;
        mIconServerId = iconServerId;
        mIconFarmId = iconFarmId;
    }

    public UserItem(Cursor cursor){
        mId = cursor.getString(cursor.getColumnIndex(PicappContract.UserEntry.COLUMN_USER_ID));
        mNsid = cursor.getString(cursor.getColumnIndex(PicappContract.UserEntry.COLUMN_USER_NSID));
        mIconServerId = cursor.getString(cursor.getColumnIndex(PicappContract.UserEntry.COLUMN_ICON_SERVER));
        mIconFarmId = cursor.getString(cursor.getColumnIndex(PicappContract.UserEntry.COLUMN_ICON_FARM_ID));
    }

    public String getIconUrl(){
        return String.format(USER_PIC_URL, mIconFarmId, mIconServerId, mNsid);
    }

    public static String getIconUrl(String iconFarm, String iconServer, String nsid){
        return String.format(USER_PIC_URL, iconFarm, iconServer, nsid);
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getNsid() {
        return mNsid;
    }

    public void setNsid(String nsid) {
        mNsid = nsid;
    }

    public String getIconServerId() {
        return mIconServerId;
    }

    public void setIconServerId(String iconServerId) {
        mIconServerId = iconServerId;
    }

    public String getIconFarmId() {
        return mIconFarmId;
    }

    public void setIconFarmId(String iconFarmId) {
        mIconFarmId = iconFarmId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mId);
        dest.writeString(mNsid);
        dest.writeString(mIconServerId);
        dest.writeString(mIconFarmId);
    }

    public static final Parcelable.Creator<UserItem> CREATOR = new Parcelable.Creator<UserItem>() {
        public UserItem createFromParcel(Parcel in) {
            return new UserItem(in);
        }

        public UserItem[] newArray(int size) {
            return new UserItem[size];
        }
    };
}
