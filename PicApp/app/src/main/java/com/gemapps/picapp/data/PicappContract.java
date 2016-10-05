package com.gemapps.picapp.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import com.gemapps.picapp.ui.model.PicItem;
import com.gemapps.picapp.ui.model.UserItem;

/**
 * Created by edu on 10/5/16.
 */

public class PicappContract  {

    public static final String CONTENT_AUTHORITY = "com.gemapps.picapp.data";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BOOKMARK = "bookmark";

    public static final class BookmarkEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_BOOKMARK).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKMARK;

        public static final String TABLE_NAME = "bookmark";

        public static final String COLUMN_OWNER_NAME = "owner_name";
        public static final String COLUMN_OWNER_NSID = "owner_nsid";
        public static final String COLUMN_OWNER_ID = "owner_id";
        public static final String COLUMN_ICON_FARM_ID = "icon_farm_id";
        public static final String COLUMN_ICON_SERVER_ID = "icon_server_id";
        public static final String COLUMN_PIC_URL = "pic_url";
        public static final String COLUMN_PIC_TITLE = "pic_title";
        public static final String COLUMN_UNIQUE_ID = "unique_id";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +

                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                COLUMN_OWNER_NAME + " TEXT NOT NULL, " +
                COLUMN_OWNER_NSID + " TEXT NOT NULL, " +
                COLUMN_OWNER_ID + " TEXT NOT NULL, " +
                COLUMN_ICON_FARM_ID + " TEXT NOT NULL," +
                COLUMN_ICON_SERVER_ID + " TEXT NOT NULL, " +
                COLUMN_PIC_URL + " TEXT NOT NULL, " +
                COLUMN_UNIQUE_ID + " TEXT NOT NULL, " +
                COLUMN_PIC_TITLE + " TEXT NOT NULL);";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static ContentValues parse(PicItem picItem, UserItem userItem){

            ContentValues contentValues = new ContentValues();

            contentValues.put(COLUMN_OWNER_ID, picItem.getOwnerId());
            contentValues.put(COLUMN_OWNER_NAME, picItem.getOwnerName());
            contentValues.put(COLUMN_OWNER_NSID, userItem.getNsid());
            contentValues.put(COLUMN_ICON_FARM_ID, userItem.getIconFarmId());
            contentValues.put(COLUMN_ICON_SERVER_ID, userItem.getIconServerId());
            contentValues.put(COLUMN_PIC_URL, picItem.getPicUrl());
            contentValues.put(COLUMN_UNIQUE_ID, buildUserUniqueId(userItem));
            contentValues.put(COLUMN_PIC_TITLE, picItem.getTitle());

            return contentValues;
        }

        public static String buildUserUniqueId(UserItem userItem){
            return userItem.getIconFarmId()+userItem.getIconServerId()+userItem.getNsid();
        }
    }

}
