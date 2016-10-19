package com.gemapps.picapp.ui.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by edu on 10/7/16.
 * User model to be used by Gson
 */
public class CommentItem {

    @SerializedName("id") private String mId;
    @SerializedName("author") private String mAuthorId;//used like nsid
    @SerializedName("realname") private String mAuthorName;
    @SerializedName("datecreate") private String mDateCreated;
    @SerializedName("iconserver") private String mIconServerId;
    @SerializedName("iconfarm") private String mIconFarmId;
    @SerializedName("_content") private String mMsg;

    public CommentItem() {
        mAuthorName = "Nobody";
    }

    public CommentItem(String id, String authorId, String authorName, String dateCreated, String iconServerId, String iconFarmId, String msg) {
        mId = id;
        mAuthorId = authorId;
        mAuthorName = authorName;
        mDateCreated = dateCreated;
        mIconServerId = iconServerId;
        mIconFarmId = iconFarmId;
        mMsg = msg;
    }

    public String getIconUrl(){
        return UserItem.getIconUrl(mIconFarmId, mIconServerId, mAuthorId);
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(String authorId) {
        mAuthorId = authorId;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String authorName) {
        mAuthorName = authorName;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public Date getPicDateFormatted(){
        Long ts = Long.parseLong(mDateCreated) * 1000L;
        return new Date(ts);
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
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

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }
}
