package com.gemapps.picapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by edu on 10/5/16.
 */

public class PicappContract  {

    public static final String CONTENT_AUTHORITY = "com.gemapps.picapp.data";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PUBLICATION = "publication";

    public static final class PublicationEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PUBLICATION).build();

        public static final String TABLE_NAME = "publication";

        public static final String COLUMN_OWNER_NAME = "owner_name";
        public static final String COLUMN_OWNER_NSID = "owner_nsid";
        public static final String COLUMN_OWNER_ID = "owner_id";
        public static final String COLUMN_FARM_ID = "farm_id";
        public static final String COLUMN_SERVER_ID = "farm_id";
        public static final String COLUMN_PIC_URL = "pic_url";
        public static final String COLUMN_PIC_TITLE = "pic_title";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +

                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                COLUMN_OWNER_NAME + " TEXT NOT NULL, " +
                COLUMN_OWNER_NSID + " TEXT NOT NULL, " +
                COLUMN_OWNER_ID + " TEXT NOT NULL, " +
                COLUMN_FARM_ID + " TEXT NOT NULL," +
                COLUMN_SERVER_ID + " TEXT NOT NULL '', " +
                COLUMN_PIC_URL + " TEXT NOT NULL, " +
                COLUMN_PIC_TITLE + " TEXT NOT NULL);";
        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
