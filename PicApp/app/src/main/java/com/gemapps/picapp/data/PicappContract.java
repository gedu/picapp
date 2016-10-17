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

    public static final String PATH_USER = "user";
    public static final String PATH_PUBLICATION = "publication";
    public static final String PATH_BOOKMARK = "bookmark";

    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" +PATH_USER;

        public static final String TABLE_NAME = "user";

        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_NSID = "user_nsid";
        public static final String COLUMN_ICON_SERVER = "icon_server";
        public static final String COLUMN_ICON_FARM_ID = "icon_farm";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +

                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                COLUMN_USER_ID + " TEXT NOT NULL, " +
                COLUMN_USER_NSID + " TEXT NOT NULL, " +
                COLUMN_ICON_SERVER + " TEXT NOT NULL, " +
                COLUMN_ICON_FARM_ID + " TEXT NOT NULL);";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static ContentValues parse(UserItem userItem){

            ContentValues contentValues = new ContentValues();

            contentValues.put(COLUMN_USER_ID, userItem.getId());
            contentValues.put(COLUMN_USER_NSID, userItem.getNsid());
            contentValues.put(COLUMN_ICON_FARM_ID, userItem.getIconFarmId());
            contentValues.put(COLUMN_ICON_SERVER, userItem.getIconServerId());

            return contentValues;
        }
    }

    public static final class PublicationEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PUBLICATION).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" +PATH_PUBLICATION;

        public static final String TABLE_NAME = "publication";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OWNER_ID = "owner_id";
        public static final String COLUMN_OWNER_NAME = "owner_name";
        public static final String COLUMN_URL_N = "url_n";
        public static final String COLUMN_COUNT_FAVES = "count_faves";
        public static final String COLUMN_COUNT_COMMENTS = "count_comments";
        public static final String COLUMN_DATE_TAKEN = "date_taken";
        public static final String COLUMN_PIC_ID = "pic_id";
        public static final String COLUMN_FARM_ID = "farm_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_SECRET_ID = "secret_id";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +

                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_OWNER_ID + " TEXT NOT NULL, " +
                COLUMN_OWNER_NAME + " TEXT NOT NULL, " +
                COLUMN_URL_N + " TEXT NOT NULL, " +
                COLUMN_COUNT_FAVES + " TEXT NOT NULL, " +
                COLUMN_COUNT_COMMENTS + " TEXT NOT NULL, " +
                COLUMN_DATE_TAKEN + " TEXT NOT NULL, " +
                COLUMN_PIC_ID + " TEXT NOT NULL, " +
                COLUMN_FARM_ID + " TEXT NOT NULL, " +
                COLUMN_SERVER_ID + " TEXT NOT NULL, " +
                COLUMN_SECRET_ID + " TEXT NOT NULL);";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static ContentValues parse(PicItem picItem){

            ContentValues contentValues = new ContentValues();

            contentValues.put(COLUMN_TITLE, picItem.getTitle());
            contentValues.put(COLUMN_OWNER_ID, picItem.getOwnerId());
            contentValues.put(COLUMN_OWNER_NAME, picItem.getOwnerName());
            contentValues.put(COLUMN_URL_N, picItem.getPicUrl());

            contentValues.put(COLUMN_COUNT_FAVES, picItem.getFaves());
            contentValues.put(COLUMN_COUNT_COMMENTS, picItem.getComments());
            contentValues.put(COLUMN_DATE_TAKEN, picItem.getPicDateTaken());
            contentValues.put(COLUMN_PIC_ID, picItem.getPicId());

            contentValues.put(COLUMN_FARM_ID, picItem.getFarm());
            contentValues.put(COLUMN_SERVER_ID, picItem.getServerId());
            contentValues.put(COLUMN_SECRET_ID, picItem.getSecretId());

            return contentValues;
        }
    }

    public static final class BookmarkEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_BOOKMARK).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKMARK;

        public static final String TABLE_NAME = "bookmark";

        public static final String COLUMN_PUBLICATION_ID = "publication_id";
        public static final String COLUMN_USER_ID = "owner_nsid";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +

                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                COLUMN_PUBLICATION_ID + " INTEGER NOT NULL, " +
                COLUMN_USER_ID + " INTEGER NOT NULL);";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static ContentValues parse(long pubId, long userId){

            ContentValues contentValues = new ContentValues();

            contentValues.put(COLUMN_PUBLICATION_ID, pubId);
            contentValues.put(COLUMN_USER_ID, userId);

            return contentValues;
        }

        public static String buildUserUniqueId(UserItem userItem){
            return userItem.getIconFarmId()+userItem.getIconServerId()+userItem.getNsid();
        }
    }

}
