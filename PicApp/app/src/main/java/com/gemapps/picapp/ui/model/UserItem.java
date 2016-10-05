package com.gemapps.picapp.ui.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by edu on 10/5/16.
 * User model to be used by Gson
 */
public class UserItem {

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

    public UserItem(String id, String nsid, String iconServerId, String iconFarmId) {
        mId = id;
        mNsid = nsid;
        mIconServerId = iconServerId;
        mIconFarmId = iconFarmId;
    }

    public String getIconUrl(){
        return String.format(USER_PIC_URL, mIconFarmId, mIconServerId, mNsid);
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
}
