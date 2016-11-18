package com.gemapps.picapp.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.gemapps.picapp.ui.model.PicItem;
import com.gemapps.picapp.ui.model.UserItem;

/**
 * Created by edu on 10/5/16.
 */

public class PicappContract  {
    private static final String TAG = "PicappContract";
    public static final String CONTENT_AUTHORITY = "com.gemapps.picapp.data";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_USER = "user";
    public static final String PATH_PUBLICATION = "publication";
    public static final String PATH_BOOKMARK = "bookmark";

    public static final String[] PROJECTION_BOOKMARK_ALL = new String[]{
    UserEntry._ID_AS, UserEntry.COLUMN_USER_ID, UserEntry.COLUMN_USER_NSID, UserEntry.COLUMN_ICON_SERVER,
            UserEntry.COLUMN_ICON_FARM_ID, PublicationEntry.COLUMN_TITLE, PublicationEntry.COLUMN_OWNER_ID,
            PublicationEntry._ID_AS, PublicationEntry.COLUMN_OWNER_NAME, PublicationEntry.COLUMN_URL_N,
            PublicationEntry.COLUMN_COUNT_FAVES, PublicationEntry.COLUMN_COUNT_COMMENTS, PublicationEntry.COLUMN_DATE_TAKEN,
            PublicationEntry.COLUMN_PIC_ID, PublicationEntry.COLUMN_FARM_ID, PublicationEntry.COLUMN_SERVER_ID,
            PublicationEntry.COLUMN_SECRET_ID, BookmarkEntry.TN_ID
    };

    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" +PATH_USER;

        public static final String TABLE_NAME = "user";

        public static final String SHORT_USER_ID = "u_id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_NSID = "user_nsid";
        public static final String COLUMN_ICON_SERVER = "icon_server";
        public static final String COLUMN_ICON_FARM_ID = "icon_farm";

        public static final String TN_ID = TABLE_NAME + "." + _ID;
        public static final String _ID_AS = TN_ID + " AS " + SHORT_USER_ID;

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

        public static long getUserDbId(SQLiteDatabase db, UserItem userItem){
            Cursor cursor = db.query(UserEntry.TABLE_NAME, new String[]{UserEntry._ID, UserEntry.COLUMN_USER_ID},
                    UserEntry.COLUMN_USER_ID+"= ?", new String[]{userItem.getId()}, null, null, null);

            if(cursor == null || cursor.getCount() == 0) return -1;

            cursor.moveToFirst();

            long dbId = cursor.getLong(cursor.getColumnIndex(UserEntry._ID));
            cursor.close();

            return dbId;
        }
    }

    public static final class PublicationEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PUBLICATION).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" +PATH_PUBLICATION;

        public static final String TABLE_NAME = "publication";

        public static final String SHORT_PUB_ID = "p_id";
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

        public static final String TN_ID = TABLE_NAME + "." + _ID;
        public static final String _ID_AS = TN_ID + " AS " + SHORT_PUB_ID;

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

        public static long getPublicationUniqueId(SQLiteDatabase db, PicItem picItem){
            Cursor cursor = db.query(PublicationEntry.TABLE_NAME, new String[]{PublicationEntry._ID, PublicationEntry.COLUMN_PIC_ID},
                    PublicationEntry.COLUMN_PIC_ID+"= ?", new String[]{picItem.getPicId()}, null, null, null);

            if(cursor == null || cursor.getCount() == 0) return -1;

            cursor.moveToFirst();

            long dbId = cursor.getLong(cursor.getColumnIndex(PublicationEntry._ID));
            cursor.close();

            return dbId;
        }
    }

    public static final class BookmarkEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_BOOKMARK).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKMARK;

        public static final String TABLE_NAME = "bookmark";

        public static final String COLUMN_USER_ID = "user_db_id";
        public static final String COLUMN_PUBLICATION_ID = "publication_db_id";

        public static final String TN_ID = TABLE_NAME + "." + _ID;
        public static final String TN_USER_ID = TABLE_NAME + "." + COLUMN_USER_ID;
        public static final String TN_PUBLICATION_ID = TABLE_NAME + "." + COLUMN_PUBLICATION_ID;

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +

                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                COLUMN_USER_ID + " INTEGER NOT NULL REFERENCES " +
                UserEntry.TABLE_NAME + "(" + UserEntry._ID + ") ON DELETE CASCADE, " +

                COLUMN_PUBLICATION_ID + " INTEGER NOT NULL REFERENCES " +
                PublicationEntry.TABLE_NAME + "(" + PublicationEntry._ID + ") ON DELETE CASCADE"+
                ");";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static ContentValues parse(long userId, long pubId){

            ContentValues contentValues = new ContentValues();

            contentValues.put(COLUMN_USER_ID, userId);
            contentValues.put(COLUMN_PUBLICATION_ID, pubId);

            return contentValues;
        }




    }

}
